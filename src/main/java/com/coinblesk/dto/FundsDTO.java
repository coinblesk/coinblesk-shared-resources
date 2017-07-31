package com.coinblesk.dto;

import java.util.List;

import lombok.Value;

@Value
public class FundsDTO {

	private String clientPublicKey;
	private String serverPublicKey;
	private Long virtualBalance;
	private long totalBalance;
	private long channelTransactionAmount;
	private boolean locked;

	private List<TimeLockedAddressDTO> timeLockedAddresses;

}
