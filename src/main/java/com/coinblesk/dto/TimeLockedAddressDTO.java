package com.coinblesk.dto;

import java.util.Date;

import lombok.Data;

@Data
public class TimeLockedAddressDTO {
	private final String bitcoinAddress;
	private final String adddressUrl;
	private final Date createdAt;
	private final Date lockedUntil;
	private final Boolean locked;
	private final String redeemScript;
	private final Long balance;
}
