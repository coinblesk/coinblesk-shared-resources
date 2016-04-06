package com.coinblesk.bitcoin;

import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKLOCKTIMEVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIG;
import static org.bitcoinj.script.ScriptOpCodes.OP_CHECKSIGVERIFY;
import static org.bitcoinj.script.ScriptOpCodes.OP_DROP;
import static org.bitcoinj.script.ScriptOpCodes.OP_ELSE;
import static org.bitcoinj.script.ScriptOpCodes.OP_ENDIF;
import static org.bitcoinj.script.ScriptOpCodes.OP_IF;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeLockedAddressTest {
	private ECKey userKey, serviceKey;
	private byte[] userPubKey, servicePubKey;
	
	private final long lockTime = 123456;
	
	// expected addresses for given keys and locktime
	private final String MAINNET_ADDRESS = "34DSzRDMVxZZsonGf3zA98vLgt5o1T4EBx";
	private final String TESTNET_ADDRESS = "2Mumf4A9P7R4v5bQpLBc2m5ubuEHxjb4jy4";
	
	private NetworkParameters defaultParams;
	
	@Before
	public void before() {
		defaultParams = MainNetParams.get();
		createKeys();
	}
	
	@After
	public void after() {
		userKey = null;
		userPubKey = null;
		serviceKey = null;
		servicePubKey = null;
		defaultParams = null;
	}
	
	private void createKeys() {
		// New keys - not random for simplicity
		userKey = ECKey.fromPrivate(Sha256Hash.hash("user-alice".getBytes()));
		userPubKey = userKey.getPubKey();
		serviceKey = ECKey.fromPrivate(Sha256Hash.hash("service-bob".getBytes()));
		servicePubKey = serviceKey.getPubKey();
	}
	
	@Test
	public void testPrint() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		System.out.println(tla.toString());
		System.out.println(tla.toStringDetailed());
	}
	
	@Test
	public void testAddressHash() {
		TimeLockedAddress tlaMainNet = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, MainNetParams.get());
		assertNotNull(tlaMainNet.getAddressHash());
		assertTrue(tlaMainNet.getAddressHash().length == 20);
		
		TimeLockedAddress tlaTestNet = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, TestNet3Params.get());
		assertNotNull(tlaTestNet.getAddressHash());
		assertTrue(tlaTestNet.getAddressHash().length == 20);
		
		// hash does not depend on network.
		assertTrue(Arrays.equals(tlaMainNet.getAddressHash(), tlaTestNet.getAddressHash()));
		assertNotEquals(tlaMainNet.getAddress(), tlaTestNet.getAddress());
	}
	
	@Test
	public void testAddressMainnet() {
		NetworkParameters params = MainNetParams.get();
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		Address p2shAddress = tla.getAddress();
		Address expectedAddr = Address.fromBase58(params, MAINNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testAddressTestnet() {
		NetworkParameters params = TestNet3Params.get();
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		Address p2shAddress = tla.getAddress();
		Address expectedAddr = Address.fromBase58(params, TESTNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testEquals() {
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		assertEquals(tThis, tOther);
		assertEquals(tThis.getAddress(), tOther.getAddress());
	}
	
	@Test
	public void testNotEquals_lockTime() {
		long lockTimeOther = lockTime+1;
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTimeOther, defaultParams);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
	}
	
	@Test
	public void testNotEquals_network() {
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, MainNetParams.get());
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, TestNet3Params.get());
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
		// address hash should still be the same!
		assertTrue(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_userkey() {
		ECKey userKeyOther = new ECKey();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		TimeLockedAddress tOther = new TimeLockedAddress(userKeyOther.getPubKey(), servicePubKey, lockTime, defaultParams);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_servicekey() {
		ECKey serviceKeyOther = new ECKey();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, serviceKeyOther.getPubKey(), lockTime, defaultParams);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_keyswitch() {
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		TimeLockedAddress tOther = new TimeLockedAddress(servicePubKey, userPubKey, lockTime, defaultParams);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testFromRedeemScript() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		Script script = tla.createRedeemScript();
		
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(script.getProgram(), defaultParams);
		assertEquals(tla, copyTla);
		assertEquals(tla.getAddress(), copyTla.getAddress());
		assertEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badData() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		
		byte[] program = tla.createRedeemScript().getProgram();
		program[4]=0x16; // random tempering with the program.
		Script wrongScript = new Script(program);
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram(), defaultParams);
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		
		Script wrongScript = ScriptBuilder.createOutputScript(new ECKey().toAddress(defaultParams));
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram(), defaultParams);
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript2() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, defaultParams);
		
		Script wrongScript = new ScriptBuilder()
				.op(OP_IF)
				.number(lockTime).op(OP_CHECKLOCKTIMEVERIFY).op(OP_DROP)
				.op(OP_ELSE)
				.data(servicePubKey).op(OP_CHECKSIGVERIFY)
				.op(OP_ENDIF)
				.data(userPubKey).op(OP_CHECKSIG)
				.build();
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram(), defaultParams);
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	
}
