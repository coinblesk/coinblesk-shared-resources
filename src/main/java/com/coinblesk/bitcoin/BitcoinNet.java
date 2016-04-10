package com.coinblesk.bitcoin;


/**
 * Enum for different bitcoin networks
 * 
 * @author rvoellmy
 *
 */
public enum BitcoinNet {

	UNITTEST, REGTEST, TESTNET, MAINNET;

	public static BitcoinNet of(String network) {
		String bitcoinNetLowerCase = network.toLowerCase();
		switch (bitcoinNetLowerCase) {
		case "regtest":
			return REGTEST;
		case "unittest":
			return UNITTEST;
		case "testnet":
			return TESTNET;
		case "mainnet":
			return MAINNET;
		default:
			throw new IllegalArgumentException("Invalid network " + network);
		}
	}
    
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}

}
