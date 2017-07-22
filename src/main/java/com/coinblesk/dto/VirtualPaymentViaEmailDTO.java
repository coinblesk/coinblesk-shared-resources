package com.coinblesk.dto;

import lombok.Data;

@Data
public class VirtualPaymentViaEmailDTO {

	private String receiverEmail;
	private Long amount;
	
}
