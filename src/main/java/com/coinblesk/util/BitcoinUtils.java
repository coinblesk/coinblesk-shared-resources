/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
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
    
    public static List<TransactionInput> convertPointsToInputs(final NetworkParameters params,
            final List<Pair<TransactionOutPoint, Coin>> outputsToUse, final Script redeemScript) {
        final List<TransactionInput> retVal = new ArrayList<>();
        for (final Pair<TransactionOutPoint, Coin> p:outputsToUse) {
            final TransactionInput ti = new TransactionInput(params, null,
                    redeemScript.getProgram(), p.element0(), p.element1());
            retVal.add(ti);
        }
        return retVal;
    }

    public static Transaction generateUnsignedRefundTx(final NetworkParameters params,
            final List<TransactionOutput> outputsToUse, List<TransactionInput> preBuiltInputs,
            final Address refundSentTo, Script redeemScript, final int lockTime) {
        final Transaction refundTransaction = new Transaction(params);
        long remainingAmount = 0;

        for (final TransactionOutput transactionOutput : outputsToUse) {
            TransactionInput ti = refundTransaction.addInput(transactionOutput);
            ti.setScriptSig(redeemScript);
            remainingAmount += transactionOutput.getValue().longValue();
        }
        if(preBuiltInputs != null) {
            for(TransactionInput input:preBuiltInputs) {
                TransactionInput ti = refundTransaction.addInput(input);
                remainingAmount += ti.getValue().longValue();
            }
        }
        
        sortTransactionInputs(refundTransaction);
        
        remainingAmount -= Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.value;
        final Coin amountToSpend = Coin.valueOf(remainingAmount);
        final TransactionOutput transactionOutput = refundTransaction.addOutput(amountToSpend, refundSentTo);
        if (amountToSpend.isLessThan(transactionOutput.getMinNonDustValue())) {
            return null;
        }
        refundTransaction.setLockTime(lockTime);
        return refundTransaction;
    }

    public static List<TransactionSignature> partiallySign(Transaction tx, Script redeemScript, ECKey signKey) {
        final int len = tx.getInputs().size();
        final List<TransactionSignature> signatures = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
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
        if (len != signatures1.size()) {
            return false;
        }
        if (len != signatures2.size()) {
            return false;
        }
        for (int i = 0; i < len; i++) {
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

    public static List<Pair<TransactionOutPoint, Coin>> outpointsFromInput(final Transaction tx) {
        final List<Pair<TransactionOutPoint, Coin>> transactionOutPoints = new ArrayList<>(tx.getInputs().size());
        for (final TransactionInput transactionInput : tx.getInputs()) {
            transactionOutPoints.add(new Pair<>(
                    transactionInput.getOutpoint(), transactionInput.getValue()));
        }
        return transactionOutPoints;
    }

    public static List<Pair<TransactionOutPoint, Coin>> outpointsFromOutputFor(NetworkParameters params, final Transaction tx, final Address p2shAddress) {
        //will be less than list.size
        final List<Pair<TransactionOutPoint, Coin>> transactionOutPoints = new ArrayList<>(tx.getOutputs().size());
        for (final TransactionOutput transactionOutput : tx.getOutputs()) {
            if (transactionOutput.getAddressFromP2SH(params).equals(p2shAddress)) {
                transactionOutPoints.add(new Pair<>(
                        transactionOutput.getOutPointFor(), transactionOutput.getValue()));
            }
        }
        return transactionOutPoints;

    }

    public static LinkedHashMap<TransactionOutPoint, Coin> convertOutPoints(
            List<TransactionOutPoint> outpoints, List<TransactionOutput> outputs) {
        if (outpoints.size() != outputs.size()) {
            return null;
        }
        LinkedHashMap<TransactionOutPoint, Coin> merged = new LinkedHashMap<>();
        Iterator<TransactionOutput> itOut = outputs.iterator();
        Iterator<TransactionOutPoint> itOutPoint = outpoints.iterator();
        while (itOut.hasNext() && itOutPoint.hasNext()) {
            merged.put(itOutPoint.next(), itOut.next().getValue());
        }

        return merged;
    }

    public static LinkedHashMap<TransactionOutPoint, Coin> convertOutPoints(
            List<TransactionOutPoint> outpoints, Transaction tx) {
        LinkedHashMap<TransactionOutPoint, Coin> merged = new LinkedHashMap<>();
        for (TransactionOutPoint outpoint : outpoints) {
            //we assume this is the right tx, we cannot compare the hash, as we don't have the full tx yet
            merged.put(outpoint, tx.getOutput(outpoint.getIndex()).getValue());
        }
        return merged;
    }

    public static List<TransactionOutput> mergeOutputs(NetworkParameters params, Transaction halfSignedTx,
            List<TransactionOutput> walletOutputs, Address ourAddress) throws Exception {
        //first remove the outputs from walletOutput that are/will be burned by halfSignedTx
        final List<TransactionOutput> newOutputs = new ArrayList<>(walletOutputs.size());
        for (TransactionOutput transactionOutput : walletOutputs) {
            boolean safeToAdd = true;
            if (!isOurP2SHAddress(params, transactionOutput, ourAddress)) {
                continue;
            }
            if (halfSignedTx == null) {
                newOutputs.add(transactionOutput);
                continue;
            }
            for (TransactionInput input : halfSignedTx.getInputs()) {
                //check if this input is connected the an output from the wallet
                if (transactionOutput.getOutPointFor().equals(input.getOutpoint())) {
                    safeToAdd = false;
                    break;
                }
            }

            if (safeToAdd) {
                newOutputs.add(transactionOutput);
            }
        }

        //then add the outputs from the halfSignedTx that will be available in the future
        for (TransactionOutput transactionOutput : halfSignedTx.getOutputs()) {
            if (isOurP2SHAddress(params, transactionOutput, ourAddress)) {
                newOutputs.add(transactionOutput);
            }
        }
        return newOutputs;
    }

    private static boolean isOurP2SHAddress(NetworkParameters params, TransactionOutput to, Address ourAddress) {
        final Address a = to.getAddressFromP2SH(params);
        if (a != null && a.equals(ourAddress)) {
            return true;
        }
        return false;
    }

    public static Transaction createTx(
            NetworkParameters params, List<TransactionOutput> outputs, Address p2shAddressFrom,
            Address p2shAddressTo, long amountToSpend) {

        final Transaction tx = new Transaction(params);
        long totalAmount = 0;
        
        List<TransactionInput> unsorted = new ArrayList<TransactionInput>(outputs.size());
        for (TransactionOutput output : outputs) {
            if (isOurP2SHAddress(params, output, p2shAddressFrom)) {
                TransactionInput ti = tx.addInput(output);
                totalAmount += output.getValue().value;
                unsorted.add(ti);
            }
        }
        //now make it deterministic
        sortTransactionInputs(tx);
       
        totalAmount -= Transaction.REFERENCE_DEFAULT_MIN_TX_FEE.value;
        if (amountToSpend > totalAmount) {
            return null;
        }
        long remainingAmount = totalAmount - amountToSpend;

        TransactionOutput transactionOutputRecipient
                = new TransactionOutput(params, tx, Coin.valueOf(amountToSpend), p2shAddressTo);
        if (!transactionOutputRecipient.getValue().isLessThan(transactionOutputRecipient.getMinNonDustValue())) {
            tx.addOutput(transactionOutputRecipient);
        }

        TransactionOutput transactionOutputChange
                = new TransactionOutput(params, tx, Coin.valueOf(remainingAmount), p2shAddressFrom);
        if (!transactionOutputChange.getValue().isLessThan(transactionOutputChange.getMinNonDustValue())) {
            tx.addOutput(transactionOutputChange); //back to sender
        }

        if (tx.getOutputs().isEmpty()) {
            return null;
        }

        return tx;
    }
    
    public static List<TransactionOutput> myOutputs(NetworkParameters params, List<TransactionOutput> allOutputs, Address p2shAddress) {
        final List<TransactionOutput> myOutputs = new ArrayList<>(allOutputs.size()/2);
        for(TransactionOutput transactionOutput:allOutputs) {
            if(transactionOutput.getAddressFromP2SH(params).equals(p2shAddress)) {
                myOutputs.add(transactionOutput);
            }
        }
        return myOutputs;
    }
    
    public static List<TransactionInput> sortInputs(final List<TransactionInput> unsorted) {
         final List<TransactionInput> copy = new ArrayList<TransactionInput>(unsorted);
          Collections.sort(copy, new Comparator<TransactionInput>() {
            @Override
            public int compare(final TransactionInput o1, final TransactionInput o2) {
                int c = o1.getOutpoint().getHash().compareTo(o2.getOutpoint().getHash());
                if(c!=0) {
                    return c;
                }
                return Long.compare(o1.getOutpoint().getIndex(), o2.getOutpoint().getIndex());
            }
        });
        return copy;
    }
    
    public static List<TransactionOutput> sortOutputs(final List<TransactionOutput> unsorted) {
        final List<TransactionOutput> copy = new ArrayList<TransactionOutput>(unsorted);
        Collections.sort(copy, new Comparator<TransactionOutput>() {
            @Override
            public int compare(final TransactionOutput o1, final TransactionOutput o2) {
                final byte[] left = o1.unsafeBitcoinSerialize();
                final byte[] right = o2.unsafeBitcoinSerialize();
                for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                    final int a = (left[i] & 0xff);
                    final int b = (right[j] & 0xff);
                    if (a != b) {
                        return a - b;
                    }
                }
                return left.length - right.length;       
            }
        });
        return copy;
     }

    private static void sortTransactionInputs(Transaction tx) {
         //now make it deterministic
         List<TransactionInput> sorted = sortInputs(tx.getInputs());
         tx.clearInputs();
         for(TransactionInput transactionInput:sorted) {
             tx.addInput(transactionInput);
         }
         
    }
}
