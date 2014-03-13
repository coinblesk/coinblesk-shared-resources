package ch.uzh.csg.mbps.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Transaction implements Serializable {
	private static final long serialVersionUID = -2634613415886226452L;
	
	private long transactionNrBuyer;
	private long transactionNrSeller;
	private String buyerUsername;
	private String sellerUsername;
	private BigDecimal amount;
	private String inputCurrency;
	private BigDecimal amountInputCurrency;

	public Transaction() {
	}
	
	public Transaction(long transactionNrBuyer, long transactionNrSeller, String buyerUsername, String sellerUsername, BigDecimal amount,
			String inputCurrency, BigDecimal amountInputCurrency) {
		this.transactionNrBuyer = transactionNrBuyer;
		this.transactionNrSeller = transactionNrSeller;
		this.buyerUsername = buyerUsername;
		this.sellerUsername = sellerUsername;
		this.amount = amount;
		this.inputCurrency = inputCurrency;
		this.amountInputCurrency = amountInputCurrency;
	}
	
	public long getTransactionNrBuyer() {
		return transactionNrBuyer;
	}

	public void setTransactionNrBuyer(long transactionNr) {
		this.transactionNrBuyer = transactionNr;
	}
	
	public long getTransactionNrSeller() {
		return transactionNrSeller;
	}

	public void setTransactionNrSeller(long transactionNr) {
		this.transactionNrSeller = transactionNr;
	}
	public String getBuyerUsername() {
		return buyerUsername;
	}
	
	public void setBuyerUsername(String buyerUsername) {
		this.buyerUsername = buyerUsername;
	}

	public String getSellerUsername() {
		return sellerUsername;
	}

	public void setSellerUsername(String sellerUsername) {
		this.sellerUsername = sellerUsername;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	public String getInputCurrency() {
		return inputCurrency;
	}

	public void setInputCurrency(String inputCurrency) {
		this.inputCurrency = inputCurrency;
	}

	public BigDecimal getAmountInputCurrency() {
		return amountInputCurrency;
	}

	public void setAmountInputCurrency(BigDecimal amountInputCurrency) {
		this.amountInputCurrency = amountInputCurrency;
	}
		
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("transaction number of buyer: ");
		sb.append(getTransactionNrBuyer());
		sb.append(", transaction number of seller: ");
		sb.append(getTransactionNrSeller());
		sb.append(", buyer: ");
		sb.append(getBuyerUsername());
		sb.append(", seller: ");
		sb.append(getSellerUsername());
		sb.append(", amount: ");
		sb.append(getAmount());
		if (inputCurrency != null && !inputCurrency.isEmpty() && amountInputCurrency != null && amountInputCurrency.compareTo(BigDecimal.ZERO) >  0) {
			sb.append(" BTC, ");
			sb.append(getAmountInputCurrency());
			sb.append(" ");
			sb.append(getInputCurrency());
		} else {
			sb.append(" BTC");
		}
		return sb.toString();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		if (o == this)
			return true;

		if (!(o instanceof Transaction))
			return false;

		Transaction other = (Transaction) o;
		return new EqualsBuilder().append(getTransactionNrBuyer(), other.getTransactionNrBuyer())
				.append(getTransactionNrSeller(), other.getTransactionNrSeller())
				.append(getBuyerUsername(), other.getBuyerUsername())
				.append(getSellerUsername(), other.getSellerUsername())
				.append(getAmount(), other.getAmount())
				.append(getInputCurrency(), other.getInputCurrency())
				.append(getAmountInputCurrency(), other.getAmountInputCurrency())
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(31, 59).append(getTransactionNrBuyer())
				.append(getTransactionNrSeller())
				.append(getBuyerUsername())
				.append(getSellerUsername())
				.append(getAmount())
				.append(getInputCurrency())
				.append(getAmountInputCurrency())
				.toHashCode();
	}
	
}
