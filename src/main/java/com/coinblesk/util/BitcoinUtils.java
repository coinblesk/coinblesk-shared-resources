package com.coinblesk.util;

import com.coinblesk.bitcoin.TimeLockedAddress;
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
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitcoinUtils {

    private final static Logger LOG = LoggerFactory.getLogger(BitcoinUtils.class);
    
    public static Transaction createRefundTx(final NetworkParameters params, 
            final List<Pair<TransactionOutPoint, Coin>> refundClientPoints, final Script redeemScript,
                            Address refundSendTo, long lockTime) throws CoinbleskException, InsuffientFunds {
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
        tx.setLockTime(lockTime);
        return tx;
    }

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

    public static Transaction createSpendAllTx (
            NetworkParameters params, List<TransactionOutput> outputs,
            Address p2shAddressFrom,
            Address p2shAddressTo) throws CoinbleskException, InsuffientFunds {

        final Transaction tx = new Transaction(params);
        long totalAmount = 0;
        for (TransactionOutput output : outputs) {
            tx.addInput(output);
            totalAmount += output.getValue().getValue();
        }
        //now make it deterministic
        sortTransactionInputs(tx);

        return createTxOutputs(params, tx, totalAmount, p2shAddressFrom, p2shAddressTo, totalAmount);
    }
    
    private static Transaction createRefundTxOutputs (NetworkParameters params, Transaction tx, long totalAmount, 
            Address p2shAddressTo) throws CoinbleskException, InsuffientFunds {
        final int fee = calcFee(tx);
        LOG.debug("adding tx fee in satoshis {}", fee);
        final long remainingAmount = totalAmount - fee;
        TransactionOutput transactionOutputRecipient
                = new TransactionOutput(params, tx, Coin.valueOf(remainingAmount), p2shAddressTo);
        if (!transactionOutputRecipient.getValue().isLessThan(transactionOutputRecipient.getMinNonDustValue())) {
            tx.addOutput(transactionOutputRecipient);
        } else {
            throw new InsuffientFunds();
        }
        return tx;
    }
    
    private static Transaction createTxOutputs (NetworkParameters params, Transaction tx, long totalAmount, 
            Address p2shAddressFrom, Address p2shAddressTo, long amountToSpend) throws CoinbleskException, InsuffientFunds {

        if (amountToSpend > totalAmount) {
            throw new InsuffientFunds();
        }

        final long remainingAmount = totalAmount - amountToSpend;
        TransactionOutput transactionOutputRecipient
                = new TransactionOutput(params, tx, Coin.valueOf(amountToSpend), p2shAddressTo);
        if (!transactionOutputRecipient.getValue().isLessThan(transactionOutputRecipient.getMinNonDustValue())) {
            tx.addOutput(transactionOutputRecipient);
        } else {
            throw new CoinbleskException("Value too small, cannot create tx");
        }

        boolean hasChange = false;
        TransactionOutput transactionOutputChange
                = new TransactionOutput(params, tx, Coin.valueOf(remainingAmount), p2shAddressFrom);
        if (!transactionOutputChange.getValue().isLessThan(transactionOutputChange.getMinNonDustValue())) {
            tx.addOutput(transactionOutputChange); //back to sender
            hasChange = true;
        } else {
            LOG.warn("Change too small {}, will be used as tx fee", remainingAmount);
        }

        final int fee = calcFee(tx);
        if(hasChange){
            transactionOutputChange.setValue(transactionOutputChange.getValue().subtract(Coin.valueOf(fee)));
        } else {
            transactionOutputRecipient.setValue(transactionOutputRecipient.getValue().subtract(Coin.valueOf(fee)));
        }
        
        return tx;
    }

    public static int calcFee(Transaction tx) {
        //http://www.soroushjp.com/2014/12/20/bitcoin-multisig-the-hard-way-understanding-raw-multisignature-bitcoin-transactions/

        //scriptsig ~260 per input 2 x 71/72 per signature, rest is redeem script ~118
        //two output ~66 34/32
        //empty tx is 10 bytes
        int len = 10 + (260 * tx.getInputs().size()) + 32*tx.getOutputs().size();
        return len * 5; // instant payments can wait some hours to be confirmed, topup will not have redeem script, thus will have more than enough fee to be accepted in next block
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
    		List<TransactionOutput> outputs, Collection<Address> addressesFrom, Address changeAddress, 
            Address addressTo, long amountToSpend) 
            		throws InsuffientFunds, CoinbleskException {

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
            throw new InsuffientFunds();
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
    
    public static void setFlagsIfCLTVInputs(final Transaction tx, final Map<String, TimeLockedAddress> timeLockedAddresses, 
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
				// - input must have sequence number below maxint sequence number (default is 0xFFFFFFFF)
				input.setSequenceNumber(0);
				if (maxLockTime < tla.getLockTime()) {
					maxLockTime = tla.getLockTime();
				}
				LOG.debug("Input {} spent after lock time ({} >= {})", input, tla.getLockTime(), currentLockTimeThreshold);
			}
		}
		
		if (maxLockTime > 0) {
			tx.setLockTime(maxLockTime);
			LOG.debug("Set Transaction nLockeTime={}", maxLockTime);
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
