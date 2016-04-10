
package com.coinblesk.bitcoin;

import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKLOCKTIMEVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIG;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIGVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_DROP;
import static org.bitcoinj.script.ScriptOpCodes.OP_ELSE;
import static org.bitcoinj.script.ScriptOpCodes.OP_ENDIF;
import static org.bitcoinj.script.ScriptOpCodes.OP_IF;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptChunk;

import com.coinblesk.util.BitcoinUtils;


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
	private final byte[] addressHash;
	
	public TimeLockedAddress(byte[] userPubKey, byte[] servicePubKey, long lockTime) {
		if (userPubKey == null || !ECKey.isPubKeyCanonical(userPubKey)) {
			throw new IllegalArgumentException("userPubKey not valid.");
		}
		if (servicePubKey == null || !ECKey.isPubKeyCanonical(servicePubKey)) {
			throw new IllegalArgumentException("servicePubKey not valid.");
		}
		if (lockTime <= 0) {
			throw new IllegalArgumentException("lockTime cannot be zero or negative.");
		}
		
		this.userPubKey = userPubKey;
		this.servicePubKey = servicePubKey;
		this.lockTime = lockTime;
		this.addressHash = createAddressHash();
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
	
	public Address getAddress(NetworkParameters params) {
		final Address address = Address.fromP2SHHash(params, addressHash);
		// Alternative: 
		// final Script pubkeyScript = createPubkeyScript();
		// final Address address = Address.fromP2SHScript(params, pubkeyScript);
		return address;
	}
	
	public byte[] getAddressHash() {
		return addressHash;
	}
	
	/**
	 * Creates the hash of the redeem script (Hash160)
	 * 
	 * @return script hash
	 */
	private byte[] createAddressHash() {
		final Script redeem = createRedeemScript();
		final byte[] hash = Utils.sha256hash160(redeem.getProgram());
		return hash;
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
	public Script createPubkeyScript() {
		final Script pubkeyScript = ScriptBuilder.createP2SHOutputScript(addressHash);
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
	public Script createRedeemScript() {
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
	
	public static TimeLockedAddress fromRedeemScript(String scriptHex) {
		byte[] scriptRaw = Utils.HEX.decode(scriptHex);
		return fromRedeemScript(scriptRaw);
	}
	
	/**
	 * Transforms a redeem script (see createRedeemScript) into a TimeLockedAddress by 
	 * extracting the individual script chunks.
	 * 
	 * @param scriptRaw raw script program
	 * @return new time locked address
	 * @throws IllegalArgumentException If script cannot be converted into TimeLockedAddress.
	 */
	public static TimeLockedAddress fromRedeemScript(byte[] scriptRaw) {
		final Script script = new Script(scriptRaw);
		if (hasExpectedStructure(script)) {
			// script format is correct. now extract pushdata
			List<ScriptChunk> chunks = script.getChunks();
			ECKey serviceK = ECKey.fromPublicOnly(chunks.get(1).data);
			byte[] servicePubKey = serviceK.getPubKey();
			ECKey userK = ECKey.fromPublicOnly(chunks.get(8).data);
			byte[] userPubKey = userK.getPubKey();
			long locktime = Utils.decodeMPI(Utils.reverseBytes(chunks.get(4).data), false).longValue();
			
			return new TimeLockedAddress(userPubKey, servicePubKey, locktime);
		} else {
			throw new IllegalArgumentException("Script is not a redeemScript of TimeLockedAddress.");
		}
	}
	
	/* verifies that the opcodes are present and equal to the expected codes. */
	private static boolean hasExpectedStructure(Script script) {
		// TODO: maybe there is a nicer way to check the script structure.
		final List<ScriptChunk> chunks = script.getChunks();
		if (
				chunks.size() == 10 		&&
				/*IF*/ 
				chunks.get(0).isOpCode() 	&& chunks.get(0).equalsOpCode(OP_IF) &&
				/*servicePubKey*/
				chunks.get(1).isPushData() 	&& 
				/*CHECKSIGVERIFY*/
				chunks.get(2).isOpCode() 	&& chunks.get(2).equalsOpCode(OP_CHECKSIGVERIFY) && 
				/*ELSE*/
				chunks.get(3).isOpCode() 	&& chunks.get(3).equalsOpCode(OP_ELSE) && 
				/*lockTime*/
				chunks.get(4).isPushData() 	&& 
				/*CHECKLOCKTIMEVERIFY*/
				chunks.get(5).isOpCode() 	&& chunks.get(5).equalsOpCode(OP_CHECKLOCKTIMEVERIFY) &&
				/*DROP*/
				chunks.get(6).isOpCode() 	&& chunks.get(6).equalsOpCode(OP_DROP) &&
				/*ENDIF*/
				chunks.get(7).isOpCode() 	&& chunks.get(7).equalsOpCode(OP_ENDIF) &&
				/*clientPubKey*/
				chunks.get(8).isPushData() 	&&
				/*CHECKSIG*/
				chunks.get(9).isOpCode() 	&& chunks.get(9).equalsOpCode(OP_CHECKSIG)
			) {
			
			return true;
		}
		
		return false;
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
	
	private Script createScriptSig(final boolean spendAfterLockTime, final TransactionSignature... signatures) {
		final Script redeemScript = createRedeemScript();
		return createScriptSig(redeemScript.getProgram(), spendAfterLockTime, signatures);
	}
	
	/**
	 * Constructs a scriptSig for the contract. It has the following form:
	 * [sig] [sig..] [contract branch = 0|1] [serialized redeemScript]
	 * 
	 * @param redeemScriptRaw the redeem script 
	 * @param spendAfterLockTime true if spending after lock time without service signature.
	 * @param signatures the signatures, order is relevant
	 * @return scriptSig
	 */
	public static Script createScriptSig(final byte[] redeemScriptRaw, final boolean spendAfterLockTime, 
																			final TransactionSignature... signatures) {
		// IF (1, before expiry, 2 sigs) or ELSE (0, after expiry, 1 sig) branch of script
		final int branch = spendAfterLockTime ? 0 : 1;
		final ScriptBuilder sb = new ScriptBuilder();
		for (TransactionSignature sig : signatures) {
			sb.data(sig.encodeToBitcoin());
		}
		sb.smallNum(branch);
		sb.data(redeemScriptRaw);
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
				.append(addressHash, other.getAddressHash())
				.isEquals();
	}
	
	@Override
	public int hashCode() {
	     return new HashCodeBuilder()
	    		 .append(userPubKey)
	    		 .append(servicePubKey)
	    		 .append(lockTime)
	    		 .append(addressHash)
	    		 .toHashCode();
	}
	
	@Override
	public String toString() {
		return toString(null);
	}
	
	public String toString(NetworkParameters params) {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append("[");
		if (params != null) {
			sb.append("Address=").append(getAddress(params));
		} else {
			sb.append("AddressHashHex=").append(Utils.HEX.encode(addressHash));
		}
		sb.append(", lockTime=").append(lockTime).append("]");
		return sb.toString();
	}
	
	public String toStringDetailed(NetworkParameters params) {
		final Script script = createRedeemScript();
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(getClass().getName()).append("\n");
		if(params != null) {
			sb.append("\tAddress:\t")
				.append(getAddress(params)).append("\n");
		} else {
			sb.append("\tAddressHashHex:\t")
				.append(Utils.HEX.encode(addressHash)).append("\n");
		}
		
		sb.append("\tLockTime:\t")
			.append(lockTime).append(" (");
		if (BitcoinUtils.isLockTimeByTime(lockTime)) {
			sb.append(Utils.dateTimeFormat(lockTime*1000L));
		} else {
			sb.append("blockheight");
		}
		sb.append(")\n");
		
		
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
