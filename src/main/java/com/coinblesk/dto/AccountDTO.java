package com.coinblesk.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AccountDTO {
	private final String publicKeyClient;
	private final String publicKeyServer;
	private final String privateKeyServer;
	private final Date timeCreated;
	private final long virtualBalance;
	private final long satoshiBalance;
	private final long totalBalance;
}
