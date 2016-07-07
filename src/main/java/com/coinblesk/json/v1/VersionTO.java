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
package com.coinblesk.json.v1;

import com.coinblesk.bitcoin.BitcoinNet;

/**
 * @author Andreas Albrecht
 * @author Thomas Bocek
 */
public class VersionTO extends BaseTO<VersionTO> {
	private String clientVersion; //input
	private BitcoinNet bitcoinNet; // input/output
	private boolean isSupported; //output
	
	public String clientVersion() {
		return clientVersion;
	}
	
	public VersionTO clientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
		return this;
	}
	
	public boolean isSupported() {
		return isSupported;
	}
	
	public VersionTO setSupported(boolean isSupported) {
		this.isSupported = isSupported;
		return this;
	}
	
	public BitcoinNet bitcoinNet() {
		return bitcoinNet;
	}
	
	public VersionTO bitcoinNet(BitcoinNet bitcoinNet) {
		this.bitcoinNet = bitcoinNet;
		return this;
	}
}
