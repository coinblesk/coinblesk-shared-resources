package com.coinblesk.dto;

import lombok.Data;

@Data
public class MicroPaymentViaEmailDTO {

	private String receiverEmail;
	private Long amount;
	private String transaction;

}
