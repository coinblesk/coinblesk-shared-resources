package com.coinblesk.bitcoin;

import static org.junit.Assert.*;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeLockedAddressTest {
	private ECKey userKey, serviceKey;
	private byte[] userPubKey, servicePubKey;
	
	private final long lockTime = 123456;
	
	private final String MAINNET_ADDRESS = "34DSzRDMVxZZsonGf3zA98vLgt5o1T4EBx";
	private final String TESTNET_ADDRESS = "2Mumf4A9P7R4v5bQpLBc2m5ubuEHxjb4jy4";
	
	@Before
	public void before() {
		createKeys();
	}
	
	@After
	public void after() {
		userKey = null;
		userPubKey = null;
		serviceKey = null;
		servicePubKey = null;
	}
	
	private void createKeys() {
		// New keys - not random for simplicity
		userKey = ECKey.fromPrivate(Sha256Hash.hash("user-alice".getBytes()));
		userPubKey = userKey.getPubKey();
		serviceKey = ECKey.fromPrivate(Sha256Hash.hash("service-bob".getBytes()));
		servicePubKey = serviceKey.getPubKey();
	}
	
	@Test
	public void test() {
		NetworkParameters params = TestNet3Params.get();
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		System.out.println(tla.toString());
		System.out.println(tla.toStringDetailed());
	}
	
	@Test
	public void testAddressMainnet() {
		NetworkParameters params = MainNetParams.get();
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		Address p2shAddress = tla.getAddress();
		Address expectedAddr = Address.fromBase58(params, MAINNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
	}
	
	@Test
	public void testAddressTestnet() {
		NetworkParameters params = TestNet3Params.get();
		TimeLockedAddress tla = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		Address p2shAddress = tla.getAddress();
		Address expectedAddr = Address.fromBase58(params, TESTNET_ADDRESS);
		assertEquals(p2shAddress, expectedAddr);
	}
	
	@Test
	public void testEquals() {
		NetworkParameters params = MainNetParams.get();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		assertEquals(tThis, tOther);
		assertEquals(tThis.getAddress(), tOther.getAddress());
	}
	
	@Test
	public void testNotEquals_lockTime() {
		NetworkParameters params = MainNetParams.get();
		long lockTimeOther = lockTime+1;
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTimeOther, params);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
	}
	
	@Test
	public void testNotEquals_network() {
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, MainNetParams.get());
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, TestNet3Params.get());
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
	}
	
	@Test
	public void testNotEquals_userkey() {
		NetworkParameters params = MainNetParams.get();
		ECKey userKeyOther = new ECKey();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		TimeLockedAddress tOther = new TimeLockedAddress(userKeyOther.getPubKey(), servicePubKey, lockTime, params);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
	}
	
	@Test
	public void testNotEquals_servicekey() {
		NetworkParameters params = MainNetParams.get();
		ECKey serviceKeyOther = new ECKey();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		TimeLockedAddress tOther = new TimeLockedAddress(userPubKey, serviceKeyOther.getPubKey(), lockTime, params);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
	}
	
	@Test
	public void testNotEquals_keyswitch() {
		NetworkParameters params = MainNetParams.get();
		TimeLockedAddress tThis = new TimeLockedAddress(userPubKey, servicePubKey, lockTime, params);
		TimeLockedAddress tOther = new TimeLockedAddress(servicePubKey, userPubKey, lockTime, params);
		assertNotEquals(tThis, tOther);
		assertNotEquals(tThis.getAddress(), tOther.getAddress());
	}
	
}
