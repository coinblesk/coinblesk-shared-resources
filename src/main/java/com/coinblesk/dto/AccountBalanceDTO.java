package com.coinblesk.dto;

import java.util.Map;

import lombok.Data;

@Data
public class AccountBalanceDTO {

	private Long virtualBalance;
	private Long totalBalance;
	private Long channelTransactionAmount;
	private Long channelTransactionFees;
	private Map<String, Long> timeLockedAddresses;

}
