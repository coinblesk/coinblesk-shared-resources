package ch.uzh.csg.mbps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserAccount implements Serializable {
	private static final long serialVersionUID = 2498906801207411215L;
	
	private long id;
	private Date creationDate;
	private String username;
	private String email;
	private BigDecimal balance;
	private String password;
	private boolean deleted;
	private String privateKey;
	private String publicKey;
	private long transactionNumber;
	private boolean emailVerified;
	private String paymentAddress;
	
	public UserAccount() { 
		this.creationDate = new Date();
	}
	
	public UserAccount(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.creationDate = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		if (email == null)
			return "";
		else
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
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public String getPrivateKey() {
		return privateKey;
	}
	
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}
	
	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public long getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(long transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public String getPaymentAddress() {
		return paymentAddress;
	}

	public void setPaymentAddress(String paymentAddress) {
		this.paymentAddress = paymentAddress;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: ");
		sb.append(getId());
		sb.append(", username: ");
		sb.append(getUsername());
		sb.append(", email: ");
		sb.append(getEmail());
		sb.append(", isDeleted: ");
		sb.append(isDeleted());
		sb.append(", creationDate: ");
		sb.append(getCreationDate());
		sb.append(", is deleted: ");
		sb.append(isDeleted());
		sb.append(", balance: ");
		sb.append(getBalance());
		sb.append(", transaction number: ");
		sb.append(getTransactionNumber());
		sb.append(", emailVeryfied: ");
		sb.append(isEmailVerified());
		return sb.toString();
	}
}
