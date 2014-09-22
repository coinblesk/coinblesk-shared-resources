package ch.uzh.csg.mbps.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minidev.json.JSONObject;
import ch.uzh.csg.mbps.responseobject.TransferObject;

public class HistoryTransaction extends AbstractHistory {
	private static final long serialVersionUID = 7710423735485262156L;
	private static final String SPLIT_CHARACTER = "@";
	
	private String buyer;
	private String buyerServer;
	private String seller;
	private String sellerServer;
	private String inputCurrency;
	private BigDecimal inputCurrencyAmount;

	public HistoryTransaction() {
	}

	public HistoryTransaction(Date timestamp, String buyer, String seller, BigDecimal amount, String inputCurrency, BigDecimal inputCurrencyAmount,
			String buyerServer, String sellerServer) {
		this.timestamp = timestamp;
		this.buyer = buyer;
		this.seller = seller;
		this.amount = amount;
		this.inputCurrency = inputCurrency;
		this.inputCurrencyAmount = inputCurrencyAmount;
		this.buyerServer = buyerServer;
		this.sellerServer = sellerServer;
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

	public String getBuyerServer() {
		return buyerServer;
	}

	public void setBuyerServer(String buyerServer) {
		this.buyerServer = buyerServer;
	}
	
	public String getSeller() {
		return seller;
	}
	
	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getSellerSever() {
		return sellerServer;
	}

	public void setSellerServer(String sellerServer) {
		this.sellerServer = sellerServer;
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
		
		String buyer = getBuyer();
		String seller = getSeller();
		
		if(getBuyer().contains(SPLIT_CHARACTER)){			
			int indexPayer = getBuyer().indexOf(SPLIT_CHARACTER);
			buyer = getBuyer().substring(0, indexPayer);
		}
		if(getSeller().contains(SPLIT_CHARACTER)){
			int indexPayee = getSeller().indexOf(SPLIT_CHARACTER);
			seller = getSeller().substring(0, indexPayee);			
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(getTimestamp()));
		sb.append("\n");
		sb.append("From: ");
		sb.append(buyer);
		sb.append(" ,url ");
		sb.append(getBuyerServer());
		sb.append(", To: ");
		sb.append(seller);
		sb.append(" ,url ");
		sb.append(getSellerSever());
		sb.append(", Amount: ");
		sb.append(DisplayFormatBTC.format(getAmount()));
		sb.append(" BTC");
		sb.append(" CHF: ");
		sb.append(getInputCurrencyAmount());
		return sb.toString();
	}
	
	public void encode(JSONObject o) {
		super.encode(o);
		if(buyer!=null) {
			o.put("buyer", buyer);
		}
		if(buyerServer!=null) {
			o.put("buyerServer", buyerServer);
		}
		if(seller!=null) {
			o.put("seller", seller);
		}
		if(sellerServer!=null) {
			o.put("sellerServer", sellerServer);
		}
		if(inputCurrency!=null && inputCurrencyAmount!=null) {
			o.put("inputCurrency", inputCurrencyAmount + inputCurrency);
		}
    }
	
	

	public void decode(JSONObject o) {
		super.decode(o);
		setBuyer(TransferObject.toStringOrNull(o.get("buyer")));
		setBuyerServer(TransferObject.toStringOrNull(o.get("buyerServer")));
		setSeller(TransferObject.toStringOrNull(o.get("seller")));
		setSellerServer(TransferObject.toStringOrNull(o.get("sellerServer")));
		
		String s = TransferObject.toStringOrNull(o.get("inputCurrency"));
		if(s!=null) {
			Pattern pattern = Pattern.compile("[A-Za-z]");
			Matcher matcher = pattern.matcher(s);
			if(matcher.find()) {
				int start = matcher.start();
				if(start >= 0) {
					String number = s.substring(0, start);
					String currentry = s.substring(start);
					
					try {
						//TODO: scale
						setInputCurrencyAmount(new BigDecimal(number));
						setInputCurrency(currentry);
					} catch (NumberFormatException nfe) {
						setInputCurrencyAmount(BigDecimal.ZERO);
						setInputCurrency("ERR");
					}
				}
			}
		}
    }
}
