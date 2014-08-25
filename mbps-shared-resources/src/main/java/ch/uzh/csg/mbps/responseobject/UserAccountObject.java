package ch.uzh.csg.mbps.responseobject;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

public class UserAccountObject extends TransferObject {

	private Long id;
	private String username;
	private String email;
	private String password;
	private String paymentAddress;
	private BigDecimal balance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPaymentAddress() {
		return paymentAddress;
	}

	public void setPaymentAddress(String paymentAddress) {
		this.paymentAddress = paymentAddress;
	}

	public BigDecimal getBalanceBTC() {
		return balance;
	}

	public void setBalanceBTC(BigDecimal balance) {
		this.balance = balance;
	}
	
	public void encodeThis(JSONObject jsonObject) throws Exception {
		if (username != null) {
			jsonObject.put("username", username);
		}
		if (email != null) {
			jsonObject.put("email", email);
		}
		if (password != null) {
			jsonObject.put("password", password);
		}
		if (paymentAddress != null) {
			jsonObject.put("paymentAddress", paymentAddress);
		}
		if (id != null) {
			jsonObject.put("id", id);
		}
		if (balance != null) {
			jsonObject.put("balance", balance + "BTC");
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
		setUsername(toStringOrNull(o.get("username")));
		setEmail(toStringOrNull(o.get("email")));
		setPassword(toStringOrNull(o.get("password")));
		setPaymentAddress(toStringOrNull(o.get("paymentAddress")));
		setId(toLongOrNull(o.get("id")));
		setBalanceBTC(toBigDecimalOrNull(o.get("balance")));
		return o;
	}

}
