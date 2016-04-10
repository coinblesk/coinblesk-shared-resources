package com.coinblesk.util;

import com.coinblesk.bitcoin.TimeLockedAddress;
import com.google.common.primitives.UnsignedBytes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Transaction.SigHash;
import org.bitcoinj.core.TransactionInput;
import org.bitcoinj.core.TransactionOutPoint;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitcoinUtils {

    private final static Logger LOG = LoggerFactory.getLogger(BitcoinUtils.class);

    final static public int BLOCKS_PER_DAY = 24 * 6;
    
    public static int lockTimeBlockInDays(int nowInDays, int currentHeight) {
        return currentHeight + (nowInDays * BLOCKS_PER_DAY);
    }

    public static List<TransactionInput> convertPointsToInputs(final NetworkParameters params,
            final List<Pair<TransactionOutPoint, Coin>> outputsToUse, final Script redeemScript) {
        final List<TransactionInput> retVal = new ArrayList<>();
        for (final Pair<TransactionOutPoint, Coin> p : outputsToUse) {
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

        final Set<TransactionOutPoint> unique = new HashSet<>();
        for (final TransactionOutput transactionOutput : outputsToUse) {
            if (!unique.add(transactionOutput.getOutPointFor())) {
                continue;
            }
            TransactionInput ti = refundTransaction.addInput(transactionOutput);
            ti.setScriptSig(redeemScript);
            ti.setSequenceNumber(0); //we want to timelock
            remainingAmount += transactionOutput.getValue().longValue();
        }
        if (preBuiltInputs != null) {
            for (TransactionInput input : preBuiltInputs) {
                if (!unique.add(input.getOutpoint())) {
                    continue;
                }
                TransactionInput ti = refundTransaction.addInput(input);
                ti.setSequenceNumber(0); //we want to timelock
                remainingAmount += ti.getValue().longValue();
            }
        }

        sortTransactionInputs(refundTransaction);
        
        //scriptsig ~350 per input
        //one output ~25
        int len = refundTransaction.unsafeBitcoinSerialize().length + 
                25 + (350 * refundTransaction.getInputs().size());
        
        LOG.debug("expected refund tx length {}", len);
        
        //as in http://bitcoinexchangerate.org/test/fees
        //also seen in https://blockexplorer.com/tx/6eba473ee61ed470bb88af9af9bd54de0256bee4e38de2fa6e63e3a5f9de8f0c
        //https://bitcoinfees.21.co/
        //http://blockr.io/tx/info/6eba473ee61ed470bb88af9af9bd54de0256bee4e38de2fa6e63e3a5f9de8f0c
        int fee = (int) (len * 10.562);
        
        LOG.debug("adding refund tx fee in satoshis {}", fee);
        
        remainingAmount -= fee;
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
            LOG.debug("partially sign for input {}({}), redeemscript={}, sig is {}", i, tx.getInput(i),
                    redeemScript, sighash, serverSignature);
            signatures.add(serverSignature);
        }
        return signatures;
    }
    
	public static List<TransactionSignature> partiallySignTLA(Transaction tx, List<byte[]> redeemScripts, ECKey signKey) {
		final int inputLen = tx.getInputs().size();
		final List<TransactionSignature> signatures = new ArrayList<>(inputLen);
		for (int inputIndex = 0; inputIndex < inputLen; ++inputIndex) {
			TransactionSignature txSig = tx.calculateSignature(
					inputIndex, signKey, redeemScripts.get(inputIndex), SigHash.ALL, false);
			signatures.add(txSig);
			LOG.debug("Partially signed input: {}, redeemScript={}, sig={}", 
					inputIndex, tx.getInput(inputIndex), txSig);
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
    
    private static boolean isOurP2SHAddress(NetworkParameters params, 
										TransactionOutput to, Collection<Address> ourAddresses) {
        final Address a = to.getAddressFromP2SH(params);
        return a != null && ourAddresses.contains(a);
    }
    
    public static Transaction createTx(NetworkParameters params,
					    				List<TransactionOutput> outputs, Address addressFrom, 
					    				Address addressTo, long amountToSpend) {
    	List<Address> addressList = new ArrayList<>(1);
    	addressList.add(addressFrom);
    	return createTx(params, 
    			outputs, addressList, addressFrom, 
    			addressTo, amountToSpend);
    }
    
    public static Transaction createTx(NetworkParameters params, 
    		List<TransactionOutput> outputs, Collection<Address> addressesFrom, Address changeAddress, 
            Address addressTo, long amountToSpend) {

        final Transaction tx = new Transaction(params);
        long totalAmount = 0;

        List<TransactionInput> unsorted = new ArrayList<TransactionInput>(outputs.size());
        for (TransactionOutput output : outputs) {
            if (isOurP2SHAddress(params, output, addressesFrom)) {
                TransactionInput ti = tx.addInput(output);
                totalAmount += output.getValue().value;
                unsorted.add(ti);
            }
        }
        //now make it deterministic
        sortTransactionInputs(tx);
        
        //scriptsig ~350 per input
        //two output ~50
        int len = tx.unsafeBitcoinSerialize().length + 
                50 + (350 * tx.getInputs().size());

        LOG.debug("expected tx length {}", len);
        
        //as in http://bitcoinexchangerate.org/test/fees
        //also seen in https://blockexplorer.com/tx/6eba473ee61ed470bb88af9af9bd54de0256bee4e38de2fa6e63e3a5f9de8f0c
        //https://bitcoinfees.21.co/
        //http://blockr.io/tx/info/6eba473ee61ed470bb88af9af9bd54de0256bee4e38de2fa6e63e3a5f9de8f0c
        int fee = (int) (len * 10.562);
        
        LOG.debug("adding tx fee in satoshis {}", fee);
        
        totalAmount -= fee;
        if (amountToSpend > totalAmount) {
            return null;
        }
        long remainingAmount = totalAmount - amountToSpend;

        TransactionOutput transactionOutputRecipient
                = new TransactionOutput(params, tx, Coin.valueOf(amountToSpend), addressTo);
        if (!transactionOutputRecipient.getValue().isLessThan(transactionOutputRecipient.getMinNonDustValue())) {
            tx.addOutput(transactionOutputRecipient);
        }

        TransactionOutput transactionOutputChange
                = new TransactionOutput(params, tx, Coin.valueOf(remainingAmount), changeAddress);
        if (!transactionOutputChange.getValue().isLessThan(transactionOutputChange.getMinNonDustValue())) {
            tx.addOutput(transactionOutputChange); //back to sender
        }
        //sortTransactionOutputs(tx);

        if (tx.getOutputs().isEmpty()) {
            return null;
        }

        return tx;
    }
    
    public static void processCLTVInputs(final Transaction tx, final Map<String, TimeLockedAddress> timeLockedAddresses, 
																				final long currentLockTimeThreshold) {
    	final NetworkParameters params = tx.getParams();
		final List<TransactionInput> inputs = tx.getInputs();
		long maxLockTime = 0L;
		for (int i = 0; i < inputs.size(); ++i) {
			final TransactionInput input = inputs.get(i);
			String sentToAddress = input.getOutpoint().getConnectedOutput().getAddressFromP2SH(params).toString();
			TimeLockedAddress tla = timeLockedAddresses.get(sentToAddress);
			if (tla == null) {
				// coins were not sent to TimeLockedAddress
				continue;
			}
			
			// check whether this inputs requires two signatures or not.
			if (tla.getLockTime() < currentLockTimeThreshold) {
				// still below lockTime -> two signatures
				LOG.debug("Input {} spent before lock time ({} < {})", input, tla.getLockTime(), currentLockTimeThreshold);
			} else {
				// after lockTime
				// - user signature is sufficient.
				// - transaction must have lockTime set to >= lockTime of input 
				//   (i.e. max "lockTime of any input"/time locked address).
				// - input must have sequence number below maxint sequence number (default is ffff...)
				input.setSequenceNumber(0);
				if (maxLockTime < tla.getLockTime()) {
					maxLockTime = tla.getLockTime();
				}
				LOG.debug("Input {} spent after lock time ({} >= {})", input, tla.getLockTime(), currentLockTimeThreshold);
			}
			
			
		}
		
		if (maxLockTime > 0) {
			tx.setLockTime(maxLockTime);
			LOG.debug("Set nLockeTime of Transaction to {}", maxLockTime);
		}
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
		// now make it deterministic
		List<TransactionInput> sorted = sortInputs(tx.getInputs());
		tx.clearInputs();
		for (TransactionInput transactionInput : sorted) {
			tx.addInput(transactionInput);
		}
	}

	private static void sortTransactionOutputs(Transaction tx) {
		// now make it deterministic
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
    
    /**
     * Compares nLockTime and makes sure that the values are of the same type
     * i.e. compare time with time and block height with block height
     * 
     * @param lockTimeToTest
     * @param currentLockTime
     * @throws IllegalArgumentException if nLockTime types do not match or values are negative
     */
    public static boolean isBeforeLockTime(long lockTimeToTest, long currentLockTime) {
    	// check negative
    	if (lockTimeToTest < 0 || currentLockTime < 0) {
    		throw new IllegalArgumentException(String.format(
    				"Lock time must be positive, is {} and {}",
    				lockTimeToTest, currentLockTime));
    	}
    	
    	// compare lock time variants.
    	if (!(
				(isLockTimeByTime (lockTimeToTest) && isLockTimeByTime (currentLockTime)) ||
				(isLockTimeByBlock(lockTimeToTest) && isLockTimeByBlock(currentLockTime))
    		)) {
    		throw new IllegalArgumentException("Cannot compare lock time of different types (time vs. block height)");
    	}
    	
    	// now we are sure that we compare the same type, either time or block height
    	boolean isBefore = lockTimeToTest < currentLockTime;
    	return isBefore;
    }
    
    public static boolean isAfterLockTime(long lockTimeToTest, long currentLockTime) {
    	return !isBeforeLockTime(lockTimeToTest, currentLockTime);
    }
    
    public static boolean isLockTimeByBlock(long locktime) {
    	// see: https://bitcoin.org/en/developer-guide#locktime-and-sequence-number
    	// https://en.bitcoin.it/wiki/Protocol_documentation#tx
    	// Note: 0 disables locktime!
    	return locktime < Transaction.LOCKTIME_THRESHOLD;
    }
    
    public static boolean isLockTimeByTime(long locktime) {
    	return locktime >= Transaction.LOCKTIME_THRESHOLD; 
    }
}
