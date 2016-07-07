package com.coinblesk.json.v1;

import com.coinblesk.bitcoin.TimeLockedAddress;

/**
 * @author Andreas Albrecht
 */
public class TimeLockedAddressTO extends BaseTO<TimeLockedAddressTO> {

	private TimeLockedAddress address; // output
	
	private long lockTime; // input
	
	public TimeLockedAddress timeLockedAddress() {
		return address;
	}

	public TimeLockedAddressTO timeLockedAddress(TimeLockedAddress address) {
		this.address = address;
		return this;
	}
	
	public long lockTime() {
		return lockTime;
	}
	
	public TimeLockedAddressTO lockTime(long lockTime) {
		this.lockTime = lockTime;
		return this;
	}
}
