package com.coinblesk.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class VirtualPaymentViaEmailDTO {

	@NotNull
	private String receiverEmail;

	@Min(value = 0L)
	@NotNull
	private Long amount;

}
