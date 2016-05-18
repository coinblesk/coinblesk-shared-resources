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
package com.coinblesk.bitcoin;

import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKLOCKTIMEVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIG;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIGVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_DROP;
import static org.bitcoinj.script.ScriptOpCodes.OP_ELSE;
import static org.bitcoinj.script.ScriptOpCodes.OP_ENDIF;
import static org.bitcoinj.script.ScriptOpCodes.OP_IF;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
 * - Given: public key of client and server, lockTime.
 * - Before the expiry of the lockTime, two signatures are required (client and server both sign in order to spend).
 * - After the lockTime, only one signature is required (only client signs in order to spend).
 * 
 * A note about spending: spending directly after the lock time may be not possible because the Bitcoin nodes 
 * do not look at the current time but (1) have some flexibility regarding the block timestamp (up to 2h in the future) 
 * and (2) newer nodes consider the median time of the past blocks (BIP 113) for relaying.
 * Thus, nodes may consider a transaction non-final if the lock time is close to the current time.
 * - Block timestamp: https://en.bitcoin.it/wiki/Block_timestamp
 * - BIP 113 - Median time-past: https://github.com/bitcoin/bips/blob/master/bip-0113.mediawiki
 *  
 * @author Andreas Albrecht
 *
 */
public final class TimeLockedAddress {
	
	private final byte[] clientPubKey;
	private final byte[] serverPubKey;
	private final long lockTime;
	private final byte[] addressHash;
	
	public TimeLockedAddress(byte[] clientPubKey, byte[] serverPubKey, long lockTime) {
		if (clientPubKey == null || !ECKey.isPubKeyCanonical(clientPubKey)) {
			throw new IllegalArgumentException("clientPubKey not valid.");
		}
		if (serverPubKey == null || !ECKey.isPubKeyCanonical(serverPubKey)) {
			throw new IllegalArgumentException("serverPubKey not valid.");
		}
		if (lockTime <= 0) {
			throw new IllegalArgumentException("lockTime cannot be zero or negative.");
		}
		
		this.clientPubKey = clientPubKey;
		this.serverPubKey = serverPubKey;
		this.lockTime = lockTime;
		this.addressHash = createAddressHash();
	}
	
	public byte[] getClientPubKey() {
		return clientPubKey;
	}
	
	public byte[] getServerPubKey() {
		return serverPubKey;
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
	 *   <server pubkey> CHECKSIGVERIFY
	 * ELSE
	 *   <locktime> CHECKLOCKTIMEVERIFY DROP
	 * ENDIF
	 * <client pubkey> CHECKSIG
	 * 
	 * See BIP 65 / CLTV:  https://github.com/bitcoin/bips/blob/master/bip-0065.mediawiki
	 * 
	 * @return redeem script
	 */
	public Script createRedeemScript() {
		Script contract = new ScriptBuilder()
				.op(OP_IF)
				.data(serverPubKey).op(OP_CHECKSIGVERIFY)
				.op(OP_ELSE)
				.number(lockTime).op(OP_CHECKLOCKTIMEVERIFY).op(OP_DROP)
				.op(OP_ENDIF)
				.data(clientPubKey).op(OP_CHECKSIG)
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
			ECKey serverECKey = ECKey.fromPublicOnly(chunks.get(1).data);
			byte[] serverPubKey = serverECKey.getPubKey();
			ECKey clientECKey = ECKey.fromPublicOnly(chunks.get(8).data);
			byte[] clientPubKey = clientECKey.getPubKey();
			long locktime = Utils.decodeMPI(Utils.reverseBytes(chunks.get(4).data), false).longValue();
			
			return new TimeLockedAddress(clientPubKey, serverPubKey, locktime);
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
				/*serverPubKey*/
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
	 * @param clientSig signature of the client
	 * @param serverSig signature of the server
	 * @return scriptSig
	 */
	public Script createScriptSigBeforeLockTime(TransactionSignature clientSig, TransactionSignature serverSig) {
		if (clientSig == null) {
			throw new IllegalArgumentException("Transaction signature clientSig must not be null.");
		}
		if (serverSig == null) {
			throw new IllegalArgumentException(
					"Transaction signature serverSig must not be null (spending before locktime).");
		}
		return createScriptSig(false, clientSig, serverSig);
	}

	/**
	 * Creates a scriptSig that can be used in spending transactions after the locktime. 
	 * Only the signature of the client is required.
	 * 
	 * @param clientSig signature of the client
	 * @return scriptSig
	 */
	public Script createScriptSigAfterLockTime(TransactionSignature clientSig) {
		if (clientSig == null) {
			throw new IllegalArgumentException("Transaction signature clientSig must not be null.");
		}
		return createScriptSig(true, clientSig);
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
	 * @param spendAfterLockTime true if spending after lock time without server signature.
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
		return Arrays.equals(clientPubKey, other.getClientPubKey())
			&& Arrays.equals(serverPubKey, other.serverPubKey)
			&& lockTime == other.getLockTime()
			&& Arrays.equals(addressHash, other.getAddressHash());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(clientPubKey, serverPubKey, lockTime, addressHash);
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
		
		
		sb.append("\tClient Pubkey:\t")
			.append(Utils.HEX.encode(clientPubKey)).append("\n");
		sb.append("\tServer Pubkey:\t")
			.append(Utils.HEX.encode(serverPubKey)).append("\n");
		sb.append("\tScript:\t\t")
			.append(script.toString()).append("\n");
		sb.append("\tScript Hex:\t")
			.append(Utils.HEX.encode(script.getProgram())).append("\n");
		sb.append("]");
		return sb.toString();
	}
	
	
	public static class LockTimeComparator implements Comparator<TimeLockedAddress> {
		private final boolean ascending;

		public LockTimeComparator() {
			this(true);
		}

		public LockTimeComparator(boolean ascending) {
			this.ascending = ascending;
		}

		@Override
		public int compare(TimeLockedAddress lhs, TimeLockedAddress rhs) {
			if (lhs.lockTime < rhs.lockTime) {
				return ascending ? -1 : 1;
			} else if (lhs.lockTime > rhs.lockTime) {
				return ascending ? 1 : -1;
			} else {
				return 0;
			}
		}
	}

}
