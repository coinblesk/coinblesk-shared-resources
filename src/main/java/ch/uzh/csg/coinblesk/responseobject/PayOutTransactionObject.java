package ch.uzh.csg.coinblesk.responseobject;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

/**
 * This class creates a model for the payout transaction.
 */
public class PayOutTransactionObject extends TransferObject {

	private BigDecimal amount;
	private String btcAddress;

	public String getBtcAddress() {
		return btcAddress;
	}

	public void setBtcAddress(String btcAddress) {
		this.btcAddress = btcAddress;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void encodeThis(JSONObject jsonObject) throws Exception {
		if (btcAddress != null) {
			jsonObject.put("btcAddress", btcAddress);
		}
		if (amount != null) {
			jsonObject.put("amount", amount + "BTC");
		}
	}

	@Override
	public void encode(JSONObject jsonObject) throws Exception {
		super.encode(jsonObject);
		encodeThis(jsonObject);
	}

	@Override
	public JSONObject decode(String responseString) throws Exception {
		JSONObject o = super.decode(responseString);
		return decode(o);
	}

	public JSONObject decode(JSONObject o) {
		setBtcAddress(toStringOrNull(o.get("btcAddress")));
		setAmount(toBigDecimalOrNull(o.get("amount")));
		return o;
	}

}
