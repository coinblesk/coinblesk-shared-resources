package ch.uzh.csg.coinblesk.responseobject;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

public class UserAccountObject extends TransferObject {

	private BigDecimal balance;
	private String email;
	private Long id;
	private String password;
	private String paymentAddress;
	private Byte role;
	private String username;

	public Byte getRole() {
		return role;
	}

	public void setRole(Byte role) {
		this.role = role;
	}


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
		if (balance != null) {
			jsonObject.put("balance", balance + "BTC");
		}
		if (email != null) {
			jsonObject.put("email", email);
		}
		if (id != null) {
			jsonObject.put("id", id);
		}
		if (password != null) {
			jsonObject.put("password", password);
		}
		if (paymentAddress != null) {
			jsonObject.put("paymentAddress", paymentAddress);
		}
		if(role != null){
			jsonObject.put("role", role);			
		}
		if (username != null) {
			jsonObject.put("username", username);
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
		setBalanceBTC(toBigDecimalOrNull(o.get("balance")));
		setEmail(toStringOrNull(o.get("email")));
		setId(toLongOrNull(o.get("id")));
		setPassword(toStringOrNull(o.get("password")));
		setPaymentAddress(toStringOrNull(o.get("paymentAddress")));
		setUsername(toStringOrNull(o.get("username")));
		setRole(toByteOrNull(o.get("role")));
		return o;
	}

}
