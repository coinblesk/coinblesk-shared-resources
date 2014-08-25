package ch.uzh.csg.mbps.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ServerAccount implements Serializable {
	private static final long serialVersionUID = 6473930889263412427L;

	private long id;
	private String url;
	private String payinAddress;
	private String payoutAddress;
	private int trustLevel;
	private BigDecimal activeBalance;
	private BigDecimal balanceLimit;
	
	public ServerAccount(){
	}
	
	public ServerAccount(String url) {
		this.url = url;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

	public int getTrustLevel() {
		return trustLevel;
	}

	public void setTrustLevel(int trustLevel) {
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
}