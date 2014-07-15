package ch.uzh.csg.mbps.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryTransaction extends AbstractHistory {
	private static final long serialVersionUID = 7710423735485262156L;

	private String buyer;
	private String seller;
	private String inputCurrency;
	private BigDecimal inputCurrencyAmount;

	public HistoryTransaction() {
	}

	public HistoryTransaction(Date timestamp, String buyer, String seller, BigDecimal amount, String inputCurrency, BigDecimal inputCurrencyAmount) {
		this.timestamp = timestamp;
		this.buyer = buyer;
		this.seller = seller;
		this.amount = amount;
		this.inputCurrency = inputCurrency;
		this.inputCurrencyAmount = inputCurrencyAmount;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getInputCurrency() {
		return inputCurrency;
	}

	public void setInputCurrency(String inputCurrency) {
		this.inputCurrency = inputCurrency;
	}

	public BigDecimal getInputCurrencyAmount() {
		return inputCurrencyAmount;
	}

	public void setInputCurrencyAmount(BigDecimal inputCurrencyAmount) {
		this.inputCurrencyAmount = inputCurrencyAmount;
	}

	@Override
	public String toString() {
		DecimalFormat DisplayFormatBTC = new DecimalFormat("#.########");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss", Locale.getDefault());
		sdf.setTimeZone(TimeZone.getDefault());
		
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(getTimestamp()));
		sb.append("\n");
		sb.append("From: ");
		sb.append(getBuyer());
		sb.append(", To: ");
		sb.append(getSeller());
		sb.append(", Amount: ");
		sb.append(DisplayFormatBTC.format(getAmount()));
		sb.append(" BTC");
		sb.append(" CHF: ");
		sb.append(getInputCurrencyAmount());
		return sb.toString();
	}
}
