package com.coinblesk.json.v1;

public class PaymentRequestTO extends BaseTO<PaymentRequestTO> {
	
	private String address;
	private long amount;
        private int version = 0;

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
        public PaymentRequestTO version(int version) {
                this.version = version;
        return this;
        }
    
        public int version() {
                return version;
        }
	
}
