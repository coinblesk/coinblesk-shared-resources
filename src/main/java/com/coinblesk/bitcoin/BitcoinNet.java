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
