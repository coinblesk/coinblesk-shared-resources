
package com.coinblesk.bitcoin;

import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKLOCKTIMEVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIG;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIGVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_DROP;
import static org.bitcoinj.script.ScriptOpCodes.OP_ELSE;
import static org.bitcoinj.script.ScriptOpCodes.OP_ENDIF;
import static org.bitcoinj.script.ScriptOpCodes.OP_IF;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;


/**
 * Represents an address based on a time locked contract using CHECKLOCKTIMEVERIFY with the following properties:
 * - Given: public key of user and service, locktime.
 * - Before the expiry of the locktime, two signatures are required (user and service both sign in order to spend).
 * - After the locktime, only one signature is required (only user signs in order to spend).
 *  
 * @author Andreas Albrecht
 *
 */
public final class TimeLockedAddress {
	
	private final byte[] userPubKey;
	private final byte[] servicePubKey;
	private final long lockTime;
	private final Address address;
	private final NetworkParameters params;
	
	public TimeLockedAddress(byte[] userPubKey, byte[] servicePubKey, long lockTime, NetworkParameters params) {
		if (userPubKey == null || !ECKey.isPubKeyCanonical(userPubKey)) {
			throw new IllegalArgumentException("userPubKey not valid.");
		}
		if (servicePubKey == null || !ECKey.isPubKeyCanonical(servicePubKey)) {
			throw new IllegalArgumentException("servicePubKey not valid.");
		}
		if (lockTime <= 0) {
			throw new IllegalArgumentException("lockTime cannot be zero or negative.");
		}
		if (params == null) {
			throw new IllegalArgumentException("NetworkParameters cannot be null.");
		}
		
		this.userPubKey = userPubKey;
		this.servicePubKey = servicePubKey;
		this.lockTime = lockTime;
		this.params = params;
		this.address = createAddress();
	}
	
	public byte[] getUserPubKey() {
		return userPubKey;
	}
	
	public byte[] getServicePubKey() {
		return servicePubKey;
	}
	
	public long getLockTime() {
		return lockTime;
	}
	
	public Address getAddress() {
		return address;
	}

	public NetworkParameters getNetworkParameters() {
		return params;
	}
	
	/**
	 * Creates a P2SH address based on the redeem script (time locked contract).
	 * 
	 * @return the derived address
	 */
	private Address createAddress() {
		// Address = hash (sha256hash160) of redeemScript
		// hash of redeemScript is extracted from pubKeyScript
		final Script pubkeyScript = createPubkeyScript();
		final Address address = Address.fromP2SHScript(params, pubkeyScript);
		return address;
	}

	/**
	 * Creates OP_HASH160 <Hash160(redeemScript)> OP_EQUAL
	 * 
	 * For P2SH transactions, the scripts have the following form:
	 * Pubkey script: 		OP_HASH160 <Hash160(redeemScript)> OP_EQUAL
	 * Signature script: 	[sig] [sig] [sig...] <redeemScript>
	 * 
	 * See: https://bitcoin.org/en/developer-guide#standard-transactions
	 */
	private Script createPubkeyScript() {
		final Script redeemScript = createRedeemScript();
		final Script pubkeyScript = ScriptBuilder.createP2SHOutputScript(redeemScript);
		return pubkeyScript;
	}

	/**
	 * Create a redeem script with the following time locked contract:
	 *
	 * IF
	 *   <service pubkey> CHECKSIGVERIFY
	 * ELSE
	 *   <locktime> CHECKLOCKTIMEVERIFY DROP
	 * ENDIF
	 * <user pubkey> CHECKSIG
	 * 
	 * See BIP 65 / CLTV:  https://github.com/bitcoin/bips/blob/master/bip-0065.mediawiki
	 * 
	 * @return redeem script
	 */
	private Script createRedeemScript() {
		Script contract = new ScriptBuilder()
				.op(OP_IF)
				.data(servicePubKey).op(OP_CHECKSIGVERIFY)
				.op(OP_ELSE)
				.number(lockTime).op(OP_CHECKLOCKTIMEVERIFY).op(OP_DROP)
				.op(OP_ENDIF)
				.data(userPubKey).op(OP_CHECKSIG)
				.build();
		return contract;
	}
	
	/**
	 * Creates a scriptSig that can be used in spending transactions before the locktime. 
	 * Both signatures are required.
	 * 
	 * @param userSig signature of the user
	 * @param serviceSig signature of the service
	 * @return scriptSig
	 */
	public Script createScriptSigBeforeLockTime(TransactionSignature userSig, TransactionSignature serviceSig) {
		if (userSig == null) {
			throw new IllegalArgumentException("Transaction signature userSig must not be null.");
		}
		if (serviceSig == null) {
			throw new IllegalArgumentException(
					"Transaction signature serviceSig must not be null (spending before locktime).");
		}
		return createScriptSig(false, userSig, serviceSig);
	}

	/**
	 * Creates a scriptSig that can be used in spending transactions after the locktime. 
	 * Only the signature of the user is required.
	 * 
	 * @param userSig signature of the user
	 * @return scriptSig
	 */
	public Script createScriptSigAfterLockTime(TransactionSignature userSig) {
		if (userSig == null) {
			throw new IllegalArgumentException("Transaction signature userSig must not be null.");
		}
		return createScriptSig(true, userSig);
	}
	
	/**
	 * Constructs a scriptSig for the contract. It has the following form:
	 * [sig] [sig..] [contract branch = 0|1] [serialized redeemScript]
	 * 
	 * @param spendAfterLockTime true if spending after lock time without service signature.
	 * @param signatures the signatures, order is relevant
	 * @return scriptSig
	 */
	private Script createScriptSig(final boolean spendAfterLockTime, final TransactionSignature... signatures) {
		final Script redeemScript = createRedeemScript();
		// IF (1, before expiry, 2 sigs) or ELSE (0, after expiry, 1 sig) branch of script
		final int branch = spendAfterLockTime ? 0 : 1;
		final ScriptBuilder sb = new ScriptBuilder();
		for (TransactionSignature sig : signatures) {
			sb.data(sig.encodeToBitcoin());
		}
		sb.smallNum(branch);
		sb.data(redeemScript.getProgram());
		Script scriptSig = sb.build();
		return scriptSig;
	}
	
	@Override 
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		
		if (object == this) {
			return true;
		}
		
		if (!(object instanceof TimeLockedAddress)) {
			return false;
		}
		
		final TimeLockedAddress other = (TimeLockedAddress) object;
		return new EqualsBuilder()
				.append(userPubKey, other.getUserPubKey())
				.append(servicePubKey, other.getServicePubKey())
				.append(lockTime, other.getLockTime())
				.append(address, other.getAddress())
				.append(params, other.getNetworkParameters())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
	     return new HashCodeBuilder()
	    		 .append(userPubKey)
	    		 .append(servicePubKey)
	    		 .append(lockTime)
	    		 .append(address)
	    		 .append(params)
	    		 .toHashCode();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("[Address=").append(getAddress());
		sb.append(", lockTime=").append(lockTime).append("]");
		return sb.toString();
	}
	
	public String toStringDetailed() {
		final Script script = createRedeemScript();
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(getClass().getName()).append("\n");
		sb.append("\tAddress:\t")
			.append(getAddress()).append("\n");
		sb.append("\tLockTime:\t")
			.append(lockTime).append("\n");
		sb.append("\tUser Pubkey:\t")
			.append(Utils.HEX.encode(userPubKey)).append("\n");
		sb.append("\tService Pubkey:\t")
			.append(Utils.HEX.encode(servicePubKey)).append("\n");
		sb.append("\tScript:\t\t")
			.append(script.toString()).append("\n");
		sb.append("\tScript Hex:\t")
			.append(Utils.HEX.encode(script.getProgram())).append("\n");
		sb.append("]");
		return sb.toString();
	}
}
