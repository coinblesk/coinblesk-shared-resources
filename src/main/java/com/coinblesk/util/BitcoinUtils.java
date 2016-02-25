/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

/**
 *
 * @author draft
 */
public class BitcoinUtils {
    
    final static public int BLOCKS_PER_DAY = 24 * 6;
    
    public static int lockTimeBlock(int nowInDays, int currentHeight) {
        return currentHeight + (nowInDays * BLOCKS_PER_DAY);
    }
    
    public static Transaction generateUnsignedRefundTx(NetworkParameters params,
            LinkedHashMap<TransactionOutPoint, Coin> outputsToUse, 
            Address refundSentTo, Script redeemScript, int lockTime) {
        final Transaction refundTransaction = new Transaction(params);
        long remainingAmount = 0;
        
        for(final Map.Entry<TransactionOutPoint, Coin> entry:outputsToUse.entrySet()) {  
            final TransactionInput ti = new TransactionInput(params, null, 
                    redeemScript.getProgram(), entry.getKey(), entry.getValue());
            refundTransaction.addInput(ti);
            remainingAmount += entry.getValue().longValue();
        }
        remainingAmount -= Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.value;
        final Coin amountToSpend = Coin.valueOf(remainingAmount);
        final TransactionOutput transactionOutput = refundTransaction.addOutput(amountToSpend, refundSentTo);
        if(amountToSpend.isLessThan(transactionOutput.getMinNonDustValue())) {
            return null;
        }
        refundTransaction.setLockTime(lockTime);
        return refundTransaction;
    }
    
    public static List<TransactionSignature> partiallySign(Transaction tx, Script redeemScript, ECKey signKey) {
        final int len = tx.getInputs().size();
        final List<TransactionSignature> signatures = new ArrayList<>(len);
        for(int i=0;i<len;i++) {
            final Sha256Hash sighash = tx.hashForSignature(i, redeemScript, Transaction.SigHash.ALL, false);
            final TransactionSignature serverSignature = new TransactionSignature(
                    signKey.sign(sighash), Transaction.SigHash.ALL, false);
            signatures.add(serverSignature);
        }
        return signatures;
    }
    
    public static boolean applySignatures(Transaction tx, Script redeemScript, 
            List<TransactionSignature> signatures1, List<TransactionSignature> signatures2) {
        final int len = tx.getInputs().size();
        if(len != signatures1.size()) {
            return false;
        }
        if(len != signatures2.size()) {
            return false;
        }
        for(int i=0;i<len;i++) {
            List<TransactionSignature> tmp = new ArrayList<>(2);
            final TransactionSignature signature1 = signatures1.get(i);
            final TransactionSignature signature2 = signatures2.get(i);
            tmp.add(signature1);
            tmp.add(signature2);
            final Script refundTransactionInputScript = ScriptBuilder.createP2SHMultiSigInputScript(tmp, redeemScript);
            tx.getInput(i).setScriptSig(refundTransactionInputScript);
        }
        return true;
    }
    
    public static LinkedHashMap<TransactionOutPoint, Coin> mergeOutPointsValue(
            List<TransactionOutPoint> outpoins, List<TransactionOutput> outputs) {
        if(outpoins.size() != outputs.size()) {
            return null;
        }
        LinkedHashMap<TransactionOutPoint, Coin> merged = new LinkedHashMap<>();
        Iterator<TransactionOutput> itOut = outputs.iterator();
        Iterator<TransactionOutPoint> itOutPoint = outpoins.iterator();
        while(itOut.hasNext() && itOutPoint.hasNext()) {
            merged.put(itOutPoint.next(), itOut.next().getValue());
        }
        
        return merged;
    }
    
    public static List<TransactionOutput> mergeOutputs(NetworkParameters params, Transaction halfSignedTx, 
            List<TransactionOutput> walletOutputs, Address ourAddress, 
            Function<TransactionOutPoint, TransactionOutput> findParentOutput) throws Exception {
        //first remove the outputs from walletOutput that are/will be burned by halfSignedTx
        final List<TransactionOutput> newOutputs = new ArrayList<>(walletOutputs.size());
        for(TransactionOutput transactionOutput:walletOutputs) {
            boolean safeToAdd = true;
            if(!isOurP2SHAddress(params, transactionOutput, ourAddress)) {
                continue;
            } 
            for(TransactionInput input:halfSignedTx.getInputs()) {
                //if we create a transaction locally, and we use addInput(), the parentTransaction will be set for the 
                //TransactionInput and also the fromTx from in the TransactionOutPoint. input.getConnectedOutput()
                //uses fromTx, and if we have Bitcoin serialzied Tx, then we don't have this. There is the possibility
                //to call TransactionInput.connect, but this marks the Output as spent, so we would need to unmark it.
                //a better way is to search after the parent transaction for ourselfs and get the potentially burned output
                TransactionOutput burnedOutput = input.getConnectedOutput();
                //not connect, search manually
                if(burnedOutput == null) {
                    burnedOutput = findParentOutput.apply(input.getOutpoint());
                }
                if(burnedOutput == null) {
                    //something is fishy, we should be able to connect that output
                    throw new CoinbleskException("we should be able to connect that output! "
                            + "Its detached an we don't know if we can spend it");
                }
                if(transactionOutput.equals(burnedOutput)) {
                    safeToAdd = false;
                    break;
                }
            }
            
            if(safeToAdd) {
                newOutputs.add(transactionOutput);
            }
        }
        
        //then add the outputs from the halfSignedTx that will be available in the future
        for(TransactionOutput transactionOutput:halfSignedTx.getOutputs()) {
            if(isOurP2SHAddress(params, transactionOutput, ourAddress)) {
                newOutputs.add(transactionOutput);
            }
        }
        return newOutputs;
    }
    
    private static boolean isOurP2SHAddress(NetworkParameters params, TransactionOutput to, Address ourAddress) {
        final Address a = to.getAddressFromP2SH(params);
        if(a != null && a.equals(ourAddress)) {
            return true;
        }
        return false;
    }
    
    /*public static List<TransactionSignature> fullySign(Transaction tx, Script redeemScript, ECKey signKey, 
            List<TransactionSignature> existingSignatures) {
        final int len = tx.getInputs().size();
        if(len != existingSignatures.size()) {
            return null;
        }
        final List<TransactionSignature> newSignatures = new ArrayList<>(len);
        for(int i=0;i<len;i++) {
            final Sha256Hash sighash = tx.hashForSignature(i, redeemScript, Transaction.SigHash.ALL, false);
            final TransactionSignature newSignature = new TransactionSignature(
                    signKey.sign(sighash), Transaction.SigHash.ALL, false);
            newSignatures.add(newSignature);
            
            List<TransactionSignature> tmp = new ArrayList<>(2);
            final TransactionSignature existingSignature = existingSignatures.get(i);
            tmp.add(existingSignature);
            tmp.add(newSignature);
            final Script refundTransactionInputScript = ScriptBuilder.createP2SHMultiSigInputScript(tmp, redeemScript);
            tx.getInput(i).setScriptSig(refundTransactionInputScript);
        
        }
    }*/
    
    public static Pair<Transaction,List<TransactionSignature>> generateRefundTx2(NetworkParameters params,
            List<TransactionOutput> outputs, Address refund, Script redeemScript, ECKey ecKeyServer, 
            List<TransactionOutPoint> points, List<TransactionSignature> clientSigs, int lockTime) {
        final Transaction refundTransaction = new Transaction(params);
        Coin remainingAmount = Coin.ZERO;
        for(int i=0;i<outputs.size();i++) {
            TransactionOutput output = outputs.get(i);
            TransactionOutPoint outPoint = points.get(i);
            TransactionInput ti = new TransactionInput(params, null, redeemScript.getProgram(), outPoint, output.getValue());
            refundTransaction.addInput(ti);
            remainingAmount = remainingAmount.add(output.getValue());
        }
        
        remainingAmount = remainingAmount.subtract(Transaction.REFERENCE_DEFAULT_MIN_TX_FEE);
        refundTransaction.addOutput(remainingAmount, refund);
        refundTransaction.setLockTime(lockTime);
        
        List<TransactionSignature> serverSigs = new ArrayList<>();
        for(int i=0;i<refundTransaction.getInputs().size();i++) {
            final Sha256Hash sighash = refundTransaction.hashForSignature(i, redeemScript, Transaction.SigHash.ALL, false);
            final TransactionSignature serverSignature = new TransactionSignature(ecKeyServer.sign(sighash), Transaction.SigHash.ALL, false);
            serverSigs.add(serverSignature);
            if(clientSigs != null) {
                List<TransactionSignature> l = new ArrayList<>();
                final TransactionSignature clientSignature = clientSigs.get(i);
                l.add(clientSignature);
                l.add(serverSignature);
                final Script refundTransactionInputScript = ScriptBuilder.createP2SHMultiSigInputScript(l, redeemScript);
                refundTransaction.getInput(i).setScriptSig(refundTransactionInputScript);
            }
        }
        return new Pair<>(refundTransaction, serverSigs);
    }
}
