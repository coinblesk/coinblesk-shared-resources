package com.coinblesk.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MicroPaymentViaEmailDTO {

	@NotNull
	private String receiverEmail;

	@NotNull
	@Min(value = 0L)
	private Long amount;

	@NotNull
	private String transaction;

}
