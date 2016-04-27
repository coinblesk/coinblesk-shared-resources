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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Andreas Albrecht
 *
 */
public class TimeLockedAddressTest {
	private static final Logger LOG = LoggerFactory.getLogger(TimeLockedAddressTest.class);
	
	private ECKey userKey, serviceKey;
	private byte[] userPubKey, servicePubKey;
	
	// do not change, addresses below are precalculated for this locktime
	private final long lockTime = 123456;
	
	// expected addresses for given keys and locktime
	private final ECKey FIXED_USER_KEY = 
			ECKey.fromPrivate(Sha256Hash.hash("user-alice".getBytes()));
	private final ECKey FIXED_SERVER_KEY = 
			ECKey.fromPrivate(Sha256Hash.hash("service-bob".getBytes()));
	private final String MAINNET_ADDRESS = "34DSzRDMVxZZsonGf3zA98vLgt5o1T4EBx";
	private final String TESTNET_ADDRESS = "2Mumf4A9P7R4v5bQpLBc2m5ubuEHxjb4jy4";
	
	private NetworkParameters defaultParams;
	private TestNet3Params testnet;
	private MainNetParams mainnet;
	
	@Before
	public void before() {
		testnet = TestNet3Params.get();
		mainnet = MainNetParams.get();
		defaultParams = mainnet;
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
		userKey = new ECKey();
		userPubKey = userKey.getPubKey();
		serviceKey = new ECKey();
		servicePubKey = serviceKey.getPubKey();
	}
	
	@Test
	public void testPrint() {
		TimeLockedAddress tla = createTimeLockedAddress();
		LOG.info(tla.toString());
		LOG.info(tla.toString(defaultParams));
		LOG.info(tla.toStringDetailed(defaultParams));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoLockTime() {
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, 0);
		assertNull(tla);
	}
	
	@Test
	public void testAddress() {
		TimeLockedAddress tla = createTimeLockedAddress();
		Address address = tla.getAddress(defaultParams);
		assertNotNull(address);
	}

	@Test
	public void testAddressHash() {
		TimeLockedAddress tlaMainNet = createTimeLockedAddress();
		assertNotNull(tlaMainNet.getAddressHash());
		assertTrue(tlaMainNet.getAddressHash().length == 20);
		
		TimeLockedAddress tlaTestNet = createTimeLockedAddress();
		assertNotNull(tlaTestNet.getAddressHash());
		assertTrue(tlaTestNet.getAddressHash().length == 20);
		
		// hash does not depend on network.
		assertTrue(Arrays.equals(tlaMainNet.getAddressHash(), tlaTestNet.getAddressHash()));
		assertNotEquals(tlaMainNet.getAddress(mainnet), tlaTestNet.getAddress(testnet));
	}
	
	@Test
	public void testAddressMainnet() {
		TimeLockedAddress tla = new TimeLockedAddress(FIXED_USER_KEY.getPubKey(), FIXED_SERVER_KEY.getPubKey(), lockTime);
		Address p2shAddress = tla.getAddress(mainnet);
		Address expectedAddr = Address.fromBase58(mainnet, MAINNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testAddressTestnet() {
		TimeLockedAddress tla = new TimeLockedAddress(FIXED_USER_KEY.getPubKey(), FIXED_SERVER_KEY.getPubKey(), lockTime);
		Address p2shAddress = tla.getAddress(testnet);
		Address expectedAddr = Address.fromBase58(testnet, TESTNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
		assertTrue(Arrays.equals(tla.getAddressHash(), expectedAddr.getHash160()));
	}
	
	@Test
	public void testEquals() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = createTimeLockedAddress();
		assertEquals(tThis, tOther);
		assertEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
	}
	
	@Test
	public void testNotEquals_lockTime() {
		long lockTimeOther = lockTime+1;
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTimeOther);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
	}
	
	@Test
	public void testNotEquals_network() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = createTimeLockedAddress();
		assertEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(mainnet), tOther.getAddress(testnet));
		// address hash should still be the same!
		assertTrue(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_userkey() {
		ECKey userKeyOther = new ECKey();
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(userKeyOther.getPubKey(), servicePubKey, lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_servicekey() {
		ECKey serviceKeyOther = new ECKey();
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, serviceKeyOther.getPubKey(), lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testNotEquals_keyswitch() {
		TimeLockedAddress tThis = createTimeLockedAddress();
		TimeLockedAddress tOther = new TimeLockedAddress(servicePubKey, userPubKey, lockTime);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(defaultParams), tOther.getAddress(defaultParams));
		assertFalse(Arrays.equals(tThis.getAddressHash(), tOther.getAddressHash()));
	}
	
	@Test
	public void testFromRedeemScript() {
		TimeLockedAddress tla = createTimeLockedAddress();
		Script script = tla.createRedeemScript();
		
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(script.getProgram());
		assertEquals(tla, copyTla);
		assertEquals(tla.getAddress(defaultParams), copyTla.getAddress(defaultParams));
		assertEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
		
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript() {
		TimeLockedAddress tla = createTimeLockedAddress();
		
		Script wrongScript = ScriptBuilder.createOutputScript(new ECKey().toAddress(defaultParams));
		TimeLockedAddress copyTla = TimeLockedAddress.fromRedeemScript(wrongScript.getProgram());
		assertNotEquals(tla, copyTla);
		assertNotEquals(tla.createRedeemScript(), copyTla.createRedeemScript());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testFromRedeemScript_badScript2() {
		TimeLockedAddress tla = createTimeLockedAddress();
		
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
		TimeLockedAddress tla = createTimeLockedAddress();
		Address address = tla.getAddress(defaultParams);
		
		Script script = tla.createPubkeyScript();
		assertTrue(script.isPayToScriptHash());
		Address toAddress = script.getToAddress(defaultParams);
		assertEquals(address, toAddress);
	}

	/** create address with default parameters */
	private TimeLockedAddress createTimeLockedAddress() {
		return new TimeLockedAddress(userPubKey, servicePubKey, lockTime);
	}
	
}
