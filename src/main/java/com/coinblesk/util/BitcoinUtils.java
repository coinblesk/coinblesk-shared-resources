/*
 * Copyright 2016 The Coinblesk team and the CSG Group at University of Zurich
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.coinblesk.util;

import com.google.common.primitives.UnsignedBytes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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
import org.bitcoinj.core.VerificationException;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Thomas Bocek
 * @author Andreas Albrecht
 */
public class BitcoinUtils {

    private final static Logger LOG = LoggerFactory.getLogger(BitcoinUtils.class);
    public final static long ONE_BITCOIN_IN_SATOSHI = Coin.COIN.value;
    
    public static Transaction createRefundTx(final NetworkParameters params, 
            final List<Pair<TransactionOutPoint, Coin>> refundClientPoints, final Script redeemScript,
                            Address refundSendTo, long lockTimeSeconds) throws CoinbleskException, InsufficientFunds {
        final Transaction tx = new Transaction(params);
        long totalAmount = 0;

        for (final Pair<TransactionOutPoint, Coin> p : refundClientPoints) {
            if(p.element1() == null) {
                throw new CoinbleskException("Coin cannot be null");
            }
            final Coin coin = p.element1();
            final TransactionInput ti = new TransactionInput(params, null,
                    redeemScript.getProgram(), p.element0(), coin);
            ti.setSequenceNumber(0); //we want to timelock
            tx.addInput(ti);
            totalAmount += coin.getValue();
        }
        
        //now make it deterministic
        sortTransactionInputs(tx);
        createRefundTxOutputs(params, tx, totalAmount, refundSendTo);
        tx.setLockTime(lockTimeSeconds);
        return tx;
    }
    
    public static Transaction createTx(NetworkParameters params, 
    		final List<TransactionOutput> outputs, final Address changeAddress, 
    		Address addressTo, long amountToSpend, boolean senderPaysFee) 
            throws CoinbleskException, InsufficientFunds {
        
        final Transaction tx = new Transaction(params);
        long totalAmount = 0;
        
        for (TransactionOutput output : outputs) {
            tx.addInput(output);
            totalAmount += output.getValue().value;
        }
        
        //now make it deterministic
        sortTransactionInputs(tx);
        return createTxOutputs(params, tx, totalAmount, changeAddress, addressTo, 
                amountToSpend, senderPaysFee);
        
    }

    public static Transaction createTx(NetworkParameters params, 
    		final List<Pair<TransactionOutPoint, Coin>> outputsToUse, final Script redeemScript, 
                Address p2shAddressFrom, Address p2shAddressTo, long amountToSpend, boolean senderPaysFee) 
            throws CoinbleskException, InsufficientFunds {

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
        return createTxOutputs(params, tx, totalAmount, p2shAddressFrom, p2shAddressTo, amountToSpend, 
                senderPaysFee);
    }

    public static Transaction createSpendAllTx (NetworkParameters params,
    											List<TransactionOutput> outputs, Address addressTo) 
    											throws CoinbleskException, InsufficientFunds {
        final Transaction tx = new Transaction(params);
        final long feeOneOutput = calcFee(1, tx.getInputs().size());
        
        long totalAmount = 0;
        for (TransactionOutput output : outputs) {
            tx.addInput(output);
            totalAmount += output.getValue().value;
        }
        //now make it deterministic
        sortTransactionInputs(tx);
        
        Coin amountToSend = Coin.valueOf(totalAmount - feeOneOutput);
        if (!amountToSend.isPositive()) {
        	throw new CoinbleskException("Amount ("+totalAmount+") too small (does not cover fee of "+feeOneOutput+")");
        }
        
        TransactionOutput txOutRecipient = new TransactionOutput(params, tx, amountToSend, addressTo);
        checkMinValue(txOutRecipient);
        tx.addOutput(txOutRecipient);
        
        checkFee(tx);
        
        verifyTxSimple(tx);
        
        return tx;
    }
    
    private static Transaction createRefundTxOutputs (NetworkParameters params, Transaction tx, long totalAmount, 
            Address p2shAddressTo) throws CoinbleskException, InsufficientFunds {
        final int fee = calcFee(tx);
        LOG.debug("adding tx fee in satoshis {}", fee);
        final long remainingAmount = totalAmount - fee;
        TransactionOutput transactionOutputRecipient
                = new TransactionOutput(params, tx, Coin.valueOf(remainingAmount), p2shAddressTo);
        if (!transactionOutputRecipient.getValue().isLessThan(transactionOutputRecipient.getMinNonDustValue())) {
            tx.addOutput(transactionOutputRecipient);
        } else {
            throw new InsufficientFunds();
        }
        return tx;
    }
    
    private static Transaction createTxOutputs (final NetworkParameters params, final Transaction tx, 
            final long totalAmount, final Address changeAddress, final Address p2shAddressTo, 
            final long amountToSpend, final boolean senderPaysFee) throws CoinbleskException, InsufficientFunds {

        if (amountToSpend > totalAmount) {
            throw new InsufficientFunds();
        }

       
        final long remainingAmount = totalAmount - amountToSpend;
        final long feeOneOutput = calcFee(1, tx.getInputs().size());
        final long feeTwoOutput = calcFee(2, tx.getInputs().size());
        
        final Coin changeAmount = Coin.valueOf(senderPaysFee ? (remainingAmount - feeTwoOutput) : remainingAmount);
        
        final long fee;
        final long remainingDust;
        
        TransactionOutput txOutChange = null;
        if (changeAmount.isPositive() && 
                !changeAmount.isLessThan((txOutChange = new TransactionOutput(params, tx, changeAmount, changeAddress)).getMinNonDustValue())) {
            tx.addOutput(txOutChange);
            fee = feeTwoOutput;
            remainingDust = 0;
        } else if(senderPaysFee) {
            LOG.warn("Change too small {}, will be used as tx fee", changeAmount);
            if(remainingAmount - feeOneOutput < 0) {
               throw new CoinbleskException("Value "+changeAmount+" negative, cannot create tx");
            }
            fee = feeOneOutput;
            remainingDust = remainingAmount - feeOneOutput;
        } else {
            LOG.warn("Change too small {}, will be used as tx fee", changeAmount);
            fee = feeOneOutput - changeAmount.value;
            remainingDust = 0;
        }
        
        Coin amountToRecipient;
        if(senderPaysFee) {
        	amountToRecipient = Coin.valueOf(amountToSpend + remainingDust);
        } else {
        	amountToRecipient = Coin.valueOf(amountToSpend-fee);
        	if (!amountToRecipient.isPositive()) {
        		throw new CoinbleskException("Amount to small to cover fees (output: " + amountToRecipient.value);
        	}
        }
        TransactionOutput txOutRecipient = new TransactionOutput(params, tx, amountToRecipient, p2shAddressTo);
        checkMinValue(txOutRecipient);
        tx.addOutput(txOutRecipient);
        
        //failsafe if fees large
        checkFee(tx);
        
        sortTransactionOutputs(tx);
        
        verifyTxSimple(tx);
        
        return tx;
    }
    
    private static void checkFee(Transaction tx) throws CoinbleskException {
		int maxFee = tx.unsafeBitcoinSerialize().length * 750;
        if(tx.getFee().value > maxFee) {
            throw new CoinbleskException("Failsafe: fees are large: " + tx.getFee().value + " vs. " + maxFee);
        }		
	}

	private static void checkMinValue(TransactionOutput txOut) throws CoinbleskException {
        if (txOut.getValue().isLessThan(txOut.getMinNonDustValue())) {
            throw new CoinbleskException("Value "+txOut.getValue()+" too small, cannot create tx");
        }
    }

    public static int calcFee(Transaction tx) {
        return calcFee(tx.getOutputs().size(), tx.getInputs().size());
    }
    
    public static int calcFee(int nrOutputs, int nrInputs) {
        // http://bitcoinexchangerate.org/test/fees
        // https://bitcoinfees.21.co/
        // http://bitcoinfees.com/
        // http://www.soroushjp.com/2014/12/20/bitcoin-multisig-the-hard-way-understanding-raw-multisignature-bitcoin-transactions/
    	
        //scriptsig ~260 per input 2 x 71/72 per signature, rest is redeem script ~118
        //two output ~66 34/32
        //empty tx is 10 bytes
    	
    	// assume 2 outputs if none present
    	int outputs = (nrOutputs > 0) ? nrOutputs : 2;
        int len = 10 + (260 * nrInputs) + (34 * outputs);
        return len * 30; 
    }
    
    public static Transaction sign(NetworkParameters params, Transaction tx, ECKey signKey) {
        final int len = tx.getInputs().size();
        //final List<TransactionSignature> signatures = new ArrayList<TransactionSignature>(len);
        for (int i = 0; i < len; i++) {
            final TransactionSignature serverSignature = tx.calculateSignature(i, signKey, ScriptBuilder.createOutputScript(signKey.toAddress(params)),
                    SigHash.ALL, false);
            LOG.debug("fully sign for input {}({}), {}", i, tx.getInput(i),
                    serverSignature);
            final Script refundTransactionInputScript = ScriptBuilder.createInputScript(serverSignature, signKey);
            tx.getInput(i).setScriptSig(refundTransactionInputScript);
        }
        
        return tx;
    }

    public static List<TransactionSignature> partiallySign(Transaction tx, Script redeemScript, ECKey signKey) {
        final int len = tx.getInputs().size();
        final List<TransactionSignature> signatures = new ArrayList<TransactionSignature>(len);
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
    
	public static List<TransactionSignature> partiallySign(Transaction tx, List<byte[]> redeemScripts, ECKey signKey) {
		final int len = tx.getInputs().size();
		if (redeemScripts.size() != len) {
			throw new IllegalArgumentException("Number of redeemScripts must match inputs.");
		}
		final List<TransactionSignature> signatures = new ArrayList<>(len);
		for (int i = 0; i < len; ++i) {
			TransactionSignature txSig = tx.calculateSignature(i, signKey, redeemScripts.get(i), SigHash.ALL, false);
			signatures.add(txSig);
			LOG.debug("Partially signed input: {}, redeemScript={}, sig={}", i, tx.getInput(i), txSig);
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
            List<TransactionSignature> tmp = new ArrayList<TransactionSignature>(2);
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
    
    private static boolean isOurP2SHAddress(NetworkParameters params, 
										TransactionOutput to, Collection<Address> ourAddresses) {
        final Address a = to.getAddressFromP2SH(params);
        return a != null && ourAddresses.contains(a);
    }
    
    public static Transaction createTx(NetworkParameters params, 
    		List<TransactionOutput> outputs, Collection<Address> addressesFrom, Address changeAddress, 
            Address addressTo, long amountToSpend) 
            		throws InsufficientFunds, CoinbleskException {

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
      
        final int fee = calcFee(tx);
        final long changeAmount = totalAmount - amountToSpend - fee;
        LOG.debug("Tx - totalAmount={}, amountToSpend={}, fee={}, changeAmount={}", 
        		totalAmount, amountToSpend, fee, changeAmount);
        if (changeAmount < 0) {
            throw new InsufficientFunds();
        }
 
        
        TransactionOutput txOutputRecipient = new TransactionOutput(params, tx, Coin.valueOf(amountToSpend), addressTo);
        if (!txOutputRecipient.getValue().isLessThan(txOutputRecipient.getMinNonDustValue())) {
            tx.addOutput(txOutputRecipient);
        }

        TransactionOutput txOutputChange = new TransactionOutput(params, tx, Coin.valueOf(changeAmount), changeAddress);
        if (!txOutputChange.getValue().isLessThan(txOutputChange.getMinNonDustValue())) {
            tx.addOutput(txOutputChange); //back to sender
        }

        if (tx.getOutputs().isEmpty()) {
            throw new CoinbleskException("Could not create transaction.");
        }

        return tx;
    }
    
    /**
     * If the Tx spends CLTV outputs, the nLockTime flag of the transaction and the sequence number of inputs are set
     * if the outputs are spent after the CLTV lockTime.
     * 
     * @param tx transaction, will be updated.
     * @param outputTimeLocks map that maps from the pubkeyhash of the output (hex encoded) to the corresponding lock time. 
     * @param lockTimeThreshold in seconds (unix time) or block height
     */
    public static void setFlagsOfCLTVInputs(final Transaction tx, 
    										final Map<String, Long> outputTimeLocks, 
											final long lockTimeThreshold) {
    	final String tag = "setFlagsOfCLTVInputs";
		final List<TransactionInput> inputs = tx.getInputs();
		long maxLockTime = 0L;
		
		for (int i = 0; i < inputs.size(); ++i) {
			final TransactionInput input = inputs.get(i);
			// since java maps have poor support for array keys, hex encoding is used instead.
			byte[] addressHash = input.getConnectedOutput().getScriptPubKey().getPubKeyHash();
			String addressHashHex = Utils.HEX.encode(addressHash);
			Long inputLockTime = outputTimeLocks.get(addressHashHex);
			if (inputLockTime == null) {
				// if not present, assume coins were not sent to TimeLockedAddress
				continue;
			}
			
			// check whether this inputs requires two signatures or not.
			if (isBeforeLockTime(lockTimeThreshold, inputLockTime)) {
				// lock time is in the future -> two signatures required, but no nLockTime/seqNr
				LOG.debug("{} - Input {} spent before lock time (current {} < lockTime {})", 
						tag, input, lockTimeThreshold, inputLockTime);
			} else {
				// lock time is in the past, i.e. spend after lock time
				// - user signature is sufficient.
				// - transaction must have lockTime set to >= lockTime of input 
				//   (i.e. max "lockTime of any input"/time locked address).
				// - input must have sequence number below maxint sequence number (default is 0xFFFFFFFF)
				
				//   use seqNumber (max int - 1) in order to avoid replace-by-fee issues (RBF, BIP 125), see:
				//	 https://github.com/bitcoin/bips/blob/master/bip-0125.mediawiki
				input.setSequenceNumber(TransactionInput.NO_SEQUENCE - 1);
				if (maxLockTime < inputLockTime) {
					maxLockTime = inputLockTime;
				}
				LOG.debug("{} - Input {} spent after lock time (current {} >= lockTime {})", 
						tag, input, inputLockTime, lockTimeThreshold);
			}
		}
		
		if (maxLockTime > 0) {
			tx.setLockTime(maxLockTime);
			LOG.debug("{} - Set Transaction nLockTime={}", tag, maxLockTime);
		}
    }

    /**
     * Verifies the transaction and all its inputs.
     * Works only if Tx is connected (required parent of outputs). Also checks the signature
     * 
     * @param tx
     */
	public static void verifyTxFull(Transaction tx) throws CoinbleskException {
		try {
			tx.verify();
			for (TransactionInput txIn : tx.getInputs()) {
				txIn.verify();
			}
		} catch (VerificationException e) {
			throw new CoinbleskException("Transaction verification failed: " + e.getMessage(), e);
		}
	}
	
	/**
     * Checks the transaction contents for sanity, but WITHOUT its inputs.
     * 
     * @param tx
     */
	public static void verifyTxSimple(Transaction tx) throws CoinbleskException {
		try {
            tx.verify();
        } catch (VerificationException ve) {
            LOG.warn("Tx verification failed: ", ve);
            throw new CoinbleskException("Could not process transaction: " + ve.getMessage());
        }
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
    
    public static void sortTransactionOutputs(Transaction tx) {
    	List<TransactionOutput> sortedOutputs = sortOutputs(tx.getOutputs());
    	tx.clearOutputs();
    	for (TransactionOutput to : sortedOutputs) {
    		tx.addOutput(to);
    	}
    }
   
    //we are using our own comparator as the one provided by guava crashes android on some devices
    //Nexus 5 with 6.0.1 crashes with SIGBUS in libart for the getLong operation. To use the pure
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
        pubkeys = new ArrayList<ECKey>(pubkeys);
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
     * @param currentSecondsOrBlock time in seconds or block height
     * @param nLockTime time in seconds or block height
     * @return returns true if the current time is before the lockTime, i.e. lockTime holds and is in the future.
     * @throws IllegalArgumentException if nLockTime types do not match or values are negative
     */
    public static boolean isBeforeLockTime(long currentSecondsOrBlock, long nLockTime) {
    	// check negative
    	if (nLockTime < 0 || currentSecondsOrBlock < 0) {
    		throw new IllegalArgumentException(String.format(
    				"Lock time must be positive, is {} and {}",
    				nLockTime, currentSecondsOrBlock));
    	}
    	
    	// compare lock time variants.
    	if (!(
				(isLockTimeByTime (nLockTime) && isLockTimeByTime (currentSecondsOrBlock)) ||
				(isLockTimeByBlock(nLockTime) && isLockTimeByBlock(currentSecondsOrBlock))
    		)) {
    		throw new IllegalArgumentException("Cannot compare lock time of different types (time vs. block height)");
    	}
    	
    	// now we are sure that we compare the same type, either time or block height
    	boolean isBefore = currentSecondsOrBlock < nLockTime;
    	return isBefore;
    }
    
    public static boolean isAfterLockTime(long currentSecondsOrBlock, long nLockTime) {
    	return !isBeforeLockTime(currentSecondsOrBlock, nLockTime);
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
