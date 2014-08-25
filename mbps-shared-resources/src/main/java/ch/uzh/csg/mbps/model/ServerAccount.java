package ch.uzh.csg.mbps.model;

import java.io.Serializable;
import java.math.BigDecimal;

import net.minidev.json.JSONObject;
import ch.uzh.csg.mbps.responseobject.TransferObject;

public class ServerAccount implements Serializable {
	private static final long serialVersionUID = 6473930889263412427L;

	private Long id;
	private String url;
	private String payinAddress;
	private String payoutAddress;
	private Integer trustLevel;
	private BigDecimal activeBalance;
	private BigDecimal balanceLimit;
	
	public ServerAccount(){
	}
	
	public ServerAccount(String url) {
		this.url = url;
	}

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

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("id: ");
		sb.append(getId());
		sb.append(", url: ");
		sb.append(getUrl());
		sb.append(", active balance: ");
		sb.append(getActiveBalance());
		sb.append(", balance limit: ");
		sb.append(getBalanceLimit());
		sb.append(", trust level: ");
		sb.append(getTrustLevel());
		return sb.toString();
	}

	public void encode(JSONObject o) {
		if(id!=null) {
			o.put("id",id);
		}
		if(url!=null){
			o.put("url",url);
		}
		if(payinAddress!=null){
			o.put("payinAddress",payinAddress);
		}
		if(payoutAddress!=null){
			o.put("payoutAddress",payoutAddress);
		}
		if(trustLevel!=null){
			o.put("trustLevel", trustLevel);
		}
		if(activeBalance!=null){
			o.put("activeBalance", activeBalance);
		}
		if(balanceLimit!=null){
			o.put("balanceLimit", balanceLimit);
		}
    }
	
	public void decode(JSONObject o) {
		setId(TransferObject.toLongOrNull(o.get("id")));
		setUrl(TransferObject.toStringOrNull(o.get("url")));
		setPayinAddress(TransferObject.toStringOrNull(o.get("payinAddress")));
		setPayoutAddress(TransferObject.toStringOrNull(o.get("payoutAddress")));
		setTrustLevel(TransferObject.toIntOrNull(o.get("trustLevel")));
		setActiveBalance(TransferObject.toBigDecimalOrNull(o.get("activeBalance")));
		setBalanceLimit(TransferObject.toBigDecimalOrNull(o.get("balanceLimit")));
    }
	
}