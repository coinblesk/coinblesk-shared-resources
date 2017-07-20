package com.coinblesk.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PaymentRequirementsRequestDTO {

	@NotNull
	private String receiver;

	@NotNull
	private long amount;

}
