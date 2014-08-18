package ch.uzh.csg.mbps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ServerAccount implements Serializable {
	private static final long serialVersionUID = 6473930889263412427L;

	private long id;
	private String url;
	private Date creationDate;
	private String email;
	private String payinAddress;
	private int trustLevel;
	private String publicKey;
	private boolean deleted;
	private BigDecimal activeBalance;
	private BigDecimal balanceLimit;
	
	public ServerAccount(){
		this.creationDate = new Date();
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPayinAddress() {
		return payinAddress;
	}

	public void setPayinAddress(String btcAddress) {
		this.payinAddress = btcAddress;
	}

	public int getTrustLevel() {
		return trustLevel;
	}

	public void setTrustLevel(int trustLevel) {
		this.trustLevel = trustLevel;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
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
		sb.append(", email: ");
		sb.append(getEmail());
		sb.append(", isDeleted: ");
		sb.append(isDeleted());
		sb.append(", creationDate: ");
		sb.append(getCreationDate());
		sb.append(", active balance: ");
		sb.append(getActiveBalance());
		sb.append(", trust level: ");
		sb.append(getTrustLevel());
		return sb.toString();
	}
}