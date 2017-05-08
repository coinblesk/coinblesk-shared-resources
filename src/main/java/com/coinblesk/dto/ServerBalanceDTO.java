package com.coinblesk.dto;

import lombok.Data;

@Data
public class ServerBalanceDTO {

	private long sumOfAllPendingTransactions;
	private long sumOfAllVirtualBalances;
	private long serverPotBaseline;
	private long serverPotCurrent;

}
