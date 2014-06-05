package ch.uzh.csg.mbps.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PayOutRule implements Serializable {
	private static final long serialVersionUID = -2152344591629765535L;

	private int hour;
	private int day;
	private long userId;
	private BigDecimal balanceLimit;
	private String payoutAddress;

	public PayOutRule() {
	}
	
	public PayOutRule(long user, BigDecimal balance, String address){
		this.userId = user;
		this.balanceLimit = balance;
		this.payoutAddress = address;
	}
	
	public PayOutRule(long user, int hour, int day, String address){
		this.userId = user;
		this.payoutAddress = address;
		this.day = day;
		this.hour = hour;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public BigDecimal getBalanceLimit() {
		return balanceLimit;
	}

	public void setBalanceLimit(BigDecimal balanceLimit) {
		this.balanceLimit = balanceLimit;
	}

	public String getPayoutAddress() {
		return payoutAddress;
	}

	public void setPayoutAddress(String payoutAddress) {
		this.payoutAddress = payoutAddress;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" hour: ");
		sb.append(getHour());
		sb.append(" day: ");
		sb.append(getDay());
		sb.append(" userID: ");
		sb.append(getUserId());
		sb.append(" payoutAddress: ");
		sb.append(getPayoutAddress());
		sb.append(" balanceLimit: ");
		sb.append(getBalanceLimit());
		return sb.toString();
	}
}
