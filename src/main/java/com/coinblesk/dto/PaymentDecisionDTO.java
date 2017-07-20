package com.coinblesk.dto;

import lombok.Data;

@Data
public class PaymentDecisionDTO {

	public enum AddressType {
		EMAIL, BITCOIN
	};

	public enum PaymentInterface {
		DIRECT_PAYMENT, VIRTUAL_PAYMENT, MICRO_PAYMENT
	};

	private AddressType addressType;
	private PaymentInterface paymentInterface;
	private String bitcoinAddress;
	private long amount;

}
