package ch.uzh.csg.coinblesk.responseobject;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

import org.apache.commons.codec.binary.Base64;

public class TransactionObject extends TransferObject {
	
	private byte[] serverPaymentResponse = null;
	private BigDecimal balance = null;
	
	public byte[] getServerPaymentResponse() {
		return serverPaymentResponse;
	}

	public void setServerPaymentResponse(byte[] serverPaymentResponse) {
		this.serverPaymentResponse = serverPaymentResponse;
	}
	
	public BigDecimal getBalanceBTC() {
	    return balance;
    }

	public void setBalanceBTC(BigDecimal balance) {
	    this.balance = balance;
    }
	
	private String getServerPaymentResponseBase64() {
		return new String(Base64.encodeBase64(serverPaymentResponse));
	}
	
	private void setServerPaymentResponseBase64(String serverPaymentResponseBase64) {
		setServerPaymentResponse(Base64.decodeBase64(serverPaymentResponseBase64.getBytes()));
	}
	
	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		if(serverPaymentResponse != null) {
			jsonObject.put("payloadBase64", getServerPaymentResponseBase64());
		}
		if(balance !=null) {
			jsonObject.put("balance", balance+"BTC");
		}
	}
	
	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);
		String serverPaymentResponse = toStringOrNull(o.get("payloadBase64"));
		if(serverPaymentResponse!=null) {
			setServerPaymentResponseBase64(serverPaymentResponse);
		}
		setBalanceBTC(toBigDecimalOrNull(o.get("balance")));
		return o;
	}
}
