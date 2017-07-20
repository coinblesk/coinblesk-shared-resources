package com.coinblesk.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PaymentRequirementsDTO {

	@NotNull
	private String encryptedClientPrivateKey;

	@NotNull
	private List<String> previousTransactions;

	@NotNull
	private long totalLockedAndVirtualBalance;

	@NotNull
	private long currentTransactionFees;

	@NotNull
	private PaymentDecisionDTO paymentDecisionDTO;
}
