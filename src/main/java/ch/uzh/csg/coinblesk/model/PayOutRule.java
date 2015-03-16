package ch.uzh.csg.coinblesk.model;

import java.io.Serializable;
import java.math.BigDecimal;

import ch.uzh.csg.coinblesk.responseobject.TransferObject;
import net.minidev.json.JSONObject;

public class PayOutRule implements Serializable {
	private static final long serialVersionUID = -2152344591629765535L;

	private Integer hour;
	private Integer day;
	private Long userId;
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

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public BigDecimal getBalanceLimitBTC() {
		return balanceLimit;
	}

	public void setBalanceLimitBTC(BigDecimal balanceLimit) {
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
		sb.append(getBalanceLimitBTC());
		return sb.toString();
	}

	public void encode(JSONObject o) {
		if(hour!=null) {
			o.put("hour", hour);
		}
		if(day!=null) {
			o.put("day", day);
		}
		if(balanceLimit!=null) {
			o.put("balanceLimit", balanceLimit + "BTC");
		}
	    o.put("payoutAddress", payoutAddress);
	    //userId never sent like this over the network
    }

	public void decode(JSONObject o) {
		setHour(TransferObject.toIntOrNull(o.get("hour")));
		setDay(TransferObject.toIntOrNull(o.get("day")));
		setBalanceLimitBTC(TransferObject.toBigDecimalOrNull(o.get("balanceLimit")));
		setPayoutAddress(TransferObject.toStringOrNull(o.get("payoutAddress")));
    }
}
