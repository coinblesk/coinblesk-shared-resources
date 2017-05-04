package com.coinblesk.dto;

import lombok.Data;

@Data
public class ServerBalanceDTO {

	private long sumOfAllPendingTransactions;
	private long sumOfAllVirtualPayments;
	private long serverPotInitial;
	private long serverPotCurrent;

}
