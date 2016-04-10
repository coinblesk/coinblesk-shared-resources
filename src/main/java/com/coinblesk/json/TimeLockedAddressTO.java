package com.coinblesk.json;

import com.coinblesk.bitcoin.TimeLockedAddress;

/**
 * @author Andreas Albrecht
 */
public class TimeLockedAddressTO extends BaseTO<TimeLockedAddressTO> {

	private TimeLockedAddress address;
	
	public TimeLockedAddress timeLockedAddress() {
		return address;
	}

	public TimeLockedAddressTO timeLockedAddress(TimeLockedAddress address) {
		this.address = address;
		return this;
	}
}
