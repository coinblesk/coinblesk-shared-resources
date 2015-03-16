package ch.uzh.csg.coinblesk.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserAccount implements Serializable {
	private static final long serialVersionUID = 2498906801207411215L;
	
	private long id;
	private Date creationDate;
	private String username;
	private String email;
	private String password;
	private boolean emailVerified;
	private boolean deleted;
	private BigDecimal balance;
	private String paymentAddress;
	public byte roles;
	
	public UserAccount() { 
		this.creationDate = new Date();
	}
	
	public UserAccount(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
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

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getPaymentAddress() {
		return paymentAddress;
	}

	public void setPaymentAddress(String paymentAddress) {
		this.paymentAddress = paymentAddress;
	}

	public byte getRoles() {
		return roles;
	}

	public void setRoles(byte roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: ");
		sb.append(getId());
		sb.append(", creationDate: ");
		sb.append(getCreationDate());
		sb.append(", username: ");
		sb.append(getUsername());
		sb.append(", email: ");
		sb.append(getEmail());
		sb.append(", isDeleted: ");
		sb.append(isDeleted());
		sb.append(", balance: ");
		sb.append(getBalance());
		sb.append(", emailVeryfied: ");
		sb.append(isEmailVerified());
		sb.append(", roles: ");
		sb.append(getRoles());
		return sb.toString();
	}
	
}
