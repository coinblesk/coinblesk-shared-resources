package com.coinblesk.json;

public class PaymentRequestTO extends BaseTO<PaymentRequestTO> {
	
	private String address;
	private long amount;

	public String address() {
		return address;
	}
	
	public PaymentRequestTO address(String address) {
		this.address = address;
		return this;
	}
	
	public long amount() {
		return amount;
	}
	
	public PaymentRequestTO amount(long amount) {
		this.amount = amount;
		return this;
	}
	
}
