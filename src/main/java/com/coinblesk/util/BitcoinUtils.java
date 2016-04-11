/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import com.google.common.primitives.UnsignedBytes;
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
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author draft
 */
public class BitcoinUtils {

    private final static Logger LOG = LoggerFactory.getLogger(BitcoinUtils.class);

    public static Transaction createTx (
            NetworkParameters params, final List<Pair<TransactionOutPoint, Coin>> outputsToUse, 
            final Script redeemScript, Address p2shAddressFrom, Address p2shAddressTo, long amountToSpend) 
            throws CoinbleskException, InsuffientFunds {

        final Transaction tx = new Transaction(params);
        long totalAmount = 0;

        for (final Pair<TransactionOutPoint, Coin> p : outputsToUse) {
            if(p.element1() == null) {
                throw new CoinbleskException("Coin cannot be null");
            }
            final Coin coin = p.element1();
            final TransactionInput ti = new TransactionInput(params, null,
                    redeemScript.getProgram(), p.element0(), coin);
            tx.addInput(ti);
            totalAmount += coin.getValue();
        }
        
        //now make it deterministic
        sortTransactionInputs(tx);
        return createTxOutputs(params, tx, totalAmount, p2shAddressFrom, p2shAddressTo, amountToSpend);
    }
    
    public static Transaction createTx (
            NetworkParameters params, List<TransactionOutput> outputs, Address p2shAddressFrom,
            Address p2shAddressTo, long amountToSpend) throws CoinbleskException, InsuffientFunds {

        final Transaction tx = new Transaction(params);
        long totalAmount = 0;

        for (TransactionOutput output : outputs) {
            if (isOurP2SHAddress(params, output, p2shAddressFrom)) {
                tx.addInput(output);
                totalAmount += output.getValue().getValue();
            }
        }
        //now make it deterministic
        sortTransactionInputs(tx);
        return createTxOutputs(params, tx, totalAmount, p2shAddressFrom, p2shAddressTo, amountToSpend);
    }
    
    private static Transaction createTxOutputs (NetworkParameters params, Transaction tx, long totalAmount, 
            Address p2shAddressFrom, Address p2shAddressTo, long amountToSpend) throws CoinbleskException, InsuffientFunds {
        final int fee = calcFee(tx);
        LOG.debug("adding tx fee in satoshis {}", fee);
        
        totalAmount -= fee;
        if (amountToSpend > totalAmount) {
            throw new InsuffientFunds();
        }
        long remainingAmount = totalAmount - amountToSpend;

        TransactionOutput transactionOutputRecipient
                = new TransactionOutput(params, tx, Coin.valueOf(amountToSpend), p2shAddressTo);
        if (!transactionOutputRecipient.getValue().isLessThan(transactionOutputRecipient.getMinNonDustValue())) {
            tx.addOutput(transactionOutputRecipient);
        } else {
            throw new CoinbleskException("Value too small, cannot create tx");
        }

        TransactionOutput transactionOutputChange
                = new TransactionOutput(params, tx, Coin.valueOf(remainingAmount), p2shAddressFrom);
        if (!transactionOutputChange.getValue().isLessThan(transactionOutputChange.getMinNonDustValue())) {
            tx.addOutput(transactionOutputChange); //back to sender
        } else {
            LOG.warn("Change too small {}, will be used as tx fee", remainingAmount);
        }
        
        return tx;
    }
    
    private static int calcFee(Transaction tx) {
        //scriptsig ~350 per input
        //two output ~50
        final int len = tx.unsafeBitcoinSerialize().length + 
                50 + (350 * tx.getInputs().size());

        LOG.debug("expected tx length {}", len);
        
        //as in http://bitcoinexchangerate.org/test/fees
        //also seen in https://blockexplorer.com/tx/6eba473ee61ed470bb88af9af9bd54de0256bee4e38de2fa6e63e3a5f9de8f0c
        //https://bitcoinfees.21.co/
        //http://blockr.io/tx/info/6eba473ee61ed470bb88af9af9bd54de0256bee4e38de2fa6e63e3a5f9de8f0c
        final int fee = (int) (len * 10.562);
        return fee;
    }

    public static List<TransactionSignature> partiallySign(Transaction tx, Script redeemScript, ECKey signKey) {
        final int len = tx.getInputs().size();
        final List<TransactionSignature> signatures = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            final Sha256Hash sighash = tx.hashForSignature(i, redeemScript, Transaction.SigHash.ALL, false);
            final TransactionSignature serverSignature = new TransactionSignature(
                    signKey.sign(sighash), Transaction.SigHash.ALL, false);
            LOG.debug("partially sign for input {}({}), redeemscript={}, sig is {}", i, tx.getInput(i),
                    redeemScript, sighash, serverSignature);
            signatures.add(serverSignature);
        }
        return signatures;
    }

    public static boolean clientFirst(List<ECKey> keys, ECKey multisigClientKey) {
        return keys.indexOf(multisigClientKey) == 0;
    }

    public static boolean applySignatures(Transaction tx, Script redeemScript,
            List<TransactionSignature> signatures1, List<TransactionSignature> signatures2,
            boolean clientFirst) {
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
            if (clientFirst) {
                tmp.add(signature1);
                tmp.add(signature2);
            } else {
                tmp.add(signature2);
                tmp.add(signature1);
            }
            final Script refundTransactionInputScript = ScriptBuilder.createP2SHMultiSigInputScript(tmp,
                    redeemScript);
            tx.getInput(i).setScriptSig(refundTransactionInputScript);
        }
        return true;
    }

    public static List<Pair<TransactionOutPoint, Coin>> outpointsFromInput(final Transaction tx) {
        final List<Pair<TransactionOutPoint, Coin>> transactionOutPoints = new ArrayList<>(tx.getInputs()
                .size());
        for (final TransactionInput transactionInput : tx.getInputs()) {
            transactionOutPoints.add(new Pair<>(
                    transactionInput.getOutpoint(), transactionInput.getValue()));
        }
        return transactionOutPoints;
    }

    public static List<Pair<TransactionOutPoint, Coin>> outpointsFromOutputFor(NetworkParameters params,
            final Transaction tx, final Address p2shAddress) {
        //will be less than list.size
        final List<Pair<TransactionOutPoint, Coin>> transactionOutPoints = new ArrayList<>(tx.getOutputs()
                .size());
        for (final TransactionOutput transactionOutput : tx.getOutputs()) {
            if (transactionOutput.getAddressFromP2SH(params) != null
                    && transactionOutput.getAddressFromP2SH(params).equals(p2shAddress)) {
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

    

    public static List<TransactionOutput> myOutputs(NetworkParameters params,
            List<TransactionOutput> allOutputs, Address p2shAddress) {
        final List<TransactionOutput> myOutputs = new ArrayList<>(allOutputs.size() / 2);
        for (TransactionOutput transactionOutput : allOutputs) {
            if (transactionOutput.getAddressFromP2SH(params) != null
                    && transactionOutput.getAddressFromP2SH(params).equals(p2shAddress)) {
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
                final byte[] left = o1.getOutpoint().getHash().getBytes();
                final byte[] right = o2.getOutpoint().getHash().getBytes();
                for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                    final int a = (left[i] & 0xff);
                    final int b = (right[j] & 0xff);
                    if (a != b) {
                        return a - b;
                    }
                }
                int c = left.length - right.length;
                if (c != 0) {
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
        for (TransactionInput transactionInput : sorted) {
            tx.addInput(transactionInput);
        }

    }

    private static void sortTransactionOutputs(Transaction tx) {
        //now make it deterministic
        List<TransactionOutput> sorted = sortOutputs(tx.getOutputs());
        tx.clearOutputs();
        for (TransactionOutput transactionOutput : sorted) {
            tx.addOutput(transactionOutput);
        }

    }
    
    
    //we are using our own comparator as the one provided by guava crashes android on some devices
    //Nexus 5 with 6.0.1 crashes with SIGBUS in labart for the getLong operation. To use the pure
    //java comparator, we took the one from guava and set it explicitely. This hack may be solved in
    //future versions of guava

    enum PureJavaComparator implements Comparator<byte[]> {
        INSTANCE;

        @Override public int compare(byte[] left, byte[] right) {
            int minLength = Math.min(left.length, right.length);
            for (int i = 0; i < minLength; i++) {
                int result = UnsignedBytes.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }

    public static final Comparator<ECKey> PUBKEY_COMPARATOR = new Comparator<ECKey>() {
        @Override
        public int compare(ECKey k1, ECKey k2) {
            return PureJavaComparator.INSTANCE.compare(k1.getPubKey(), k2.getPubKey());
        }
    };

    public static Script createRedeemScript(int threshold, List<ECKey> pubkeys) {
        pubkeys = new ArrayList<>(pubkeys);
        Collections.sort(pubkeys, PUBKEY_COMPARATOR);
        return ScriptBuilder.createMultiSigOutputScript(threshold, pubkeys);
    }

    public static Script createP2SHOutputScript(int threshold, List<ECKey> pubkeys) {
        Script redeemScript = createRedeemScript(threshold, pubkeys);
        return createP2SHOutputScript(redeemScript);
    }

    public static Script createP2SHOutputScript(Script redeemScript) {
        byte[] hash = Utils.sha256hash160(redeemScript.getProgram());
        return ScriptBuilder.createP2SHOutputScript(hash);
    }
}
