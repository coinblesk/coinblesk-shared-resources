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
	private TestNet3Params testnet;
	private MainNetParams mainnet;
	
	@Before
	public void before() {
		defaultParams = MainNetParams.get();
		testnet = TestNet3Params.get();
		mainnet = MainNetParams.get();
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
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		System.out.println(tla.toString());
		System.out.println(tla.toString(defaultParams));
		System.out.println(tla.toStringDetailed(defaultParams));
	}
	
	@Test
	public void testAddressHash() {
		TimeLockedAddress tlaMainNet = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		assertNotNull(tlaMainNet.getAddressHash());
		assertTrue(tlaMainNet.getAddressHash().length == 20);
		
		TimeLockedAddress tlaTestNet = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		assertNotNull(tlaTestNet.getAddressHash());
		assertTrue(tlaTestNet.getAddressHash().length == 20);
		
		// hash does not depend on network.
		assertTrue(Arrays.equals(tlaMainNet.getAddressHash(), tlaTestNet.getAddressHash()));
		assertNotEquals(tlaMainNet.getAddress(mainnet), tlaTestNet.getAddress(testnet));
	}
	
	@Test
	public void testAddressMainnet() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		Address p2shAddress = tla.getAddress(mainnet);
		Address expectedAddr = Address.fromBase58(mainnet, MAINNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testAddressTestnet() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		Address p2shAddress = tla.getAddress(testnet);
		Address expectedAddr = Address.fromBase58(testnet, TESTNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testEquals() {
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		assertEquals(tThis, tOther);
		assertEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
	}
	
	@Test
	public void testNotEquals_lockTime() {
		long lockTimeOther = lockTime+1;
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTimeOther);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
	}
	
	@Test
	public void testNotEquals_network() {
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		assertEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(mainnet), tOther.getAddress(testnet));
		// address hash should still be the same!
		assertTrue(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_userkey() {
		ECKey userKeyOther = new ECKey();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		TimeLockedAddress tOther = new TimeLockedAddress(userKeyOther.getPubKey(), servicePubKey, lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_servicekey() {
		ECKey serviceKeyOther = new ECKey();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, serviceKeyOther.getPubKey(), lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_keyswitch() {
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		TimeLockedAddress tOther = new TimeLockedAddress(servicePubKey, userPubKey, lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testFromRedeemScript() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		Script script = tla.createRedeemScript();
		
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(script.getProgram());
		assertEquals(tla, copyTla);
		assertEquals(tla.getAddress(defaultParams), copyTla.getAddress(defaultParams));
		assertEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badData() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		
		byte[] program = tla.createRedeemScript().getProgram();
		program[4]=0x16; // random tempering with the program.
		Script wrongScript = new Script(program);
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram());
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		
		Script wrongScript = ScriptBuilder.createOutputScript(new ECKey().toAddress(defaultParams));
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram());
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript2() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		
		Script wrongScript = new ScriptBuilder()
				.op(OP_IF)
				.number(lockTime).op(OP_CHECKLOCKTIMEVERIFY).op(OP_DROP)
				.op(OP_ELSE)
				.data(servicePubKey).op(OP_CHECKSIGVERIFY)
				.op(OP_ENDIF)
				.data(userPubKey).op(OP_CHECKSIG)
				.build();
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram());
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test
	public void testPubkeyScript() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
		Address address = tla.getAddress(defaultParams);
		
		Address toAddress = tla.createPubkeyScript().getToAddress(defaultParams);
		
		assertEquals(address, toAddress);
	}
	
	
}
