package ch.uzh.csg.mbps.responseobject;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

public class ServerAccountObject extends TransferObject {

	private Long id;
	private String url;
	private String payinAddress;
	private String payoutAddress;
	private Integer trustLevel;
	private BigDecimal activeBalance;
	private BigDecimal balanceLimit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPayinAddress() {
		return payinAddress;
	}

	public void setPayinAddress(String btcAddress) {
		this.payinAddress = btcAddress;
	}

	public String getPayoutAddress() {
		return payoutAddress;
	}
	
	public void setPayoutAddress(String btcAddress) {
		this.payoutAddress = btcAddress;
	}

	public Integer getTrustLevel() {
		return trustLevel;
	}

	public void setTrustLevel(Integer trustLevel) {
		this.trustLevel = trustLevel;
	}

	public BigDecimal getActiveBalance() {
		return activeBalance;
	}

	public void setActiveBalance(BigDecimal activeBalance) {
		this.activeBalance = activeBalance;
	}

	public BigDecimal getBalanceLimit() {
		return balanceLimit;
	}

	public void setBalanceLimit(BigDecimal balanceLimit) {
		this.balanceLimit = balanceLimit;
	}
	
	public void encodeThis(JSONObject jsonObject) throws Exception {
		if (url != null) {
			jsonObject.put("url", url);
		}
		if (payinAddress != null) {
			jsonObject.put("payinAddress", payinAddress);
		}
		if (payoutAddress != null) {
			jsonObject.put("payoutAddress", payoutAddress);
		}
		if (trustLevel != null) {
			jsonObject.put("trustLevel", trustLevel);
		}
		if (id != null) {
			jsonObject.put("id", id);
		}
		if (activeBalance != null) {
			jsonObject.put("activeBalance", activeBalance + "BTC");
		}
		if (balanceLimit != null) {
			jsonObject.put("balanceLimit", balanceLimit + "BTC");
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
		setUrl(toStringOrNull(o.get("url")));
		setPayinAddress(toStringOrNull(o.get("payinAddress")));
		setPayoutAddress(toStringOrNull(o.get("payoutAddress")));
		setTrustLevel(toIntOrNull(o.get("trustLevel")));
		setId(toLongOrNull(o.get("id")));
		setActiveBalance(toBigDecimalOrNull(o.get("activeBalance")));
		setBalanceLimit(toBigDecimalOrNull(o.get("balanceLimit")));
		return o;
	}
}
