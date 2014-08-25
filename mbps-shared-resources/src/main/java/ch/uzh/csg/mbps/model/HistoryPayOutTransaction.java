package ch.uzh.csg.mbps.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import net.minidev.json.JSONObject;
import ch.uzh.csg.mbps.responseobject.TransferObject;

public class HistoryPayOutTransaction extends AbstractHistory {
	private static final long serialVersionUID = -8543354282567109155L;

	private String btcAddress;

	public HistoryPayOutTransaction() {
	}

	public HistoryPayOutTransaction(Date timestamp, BigDecimal amount) {
		this.timestamp = timestamp;
		this.amount = amount;
	}
	
	public HistoryPayOutTransaction(Date timestamp, BigDecimal amount, String btcAddress) {
		this.timestamp = timestamp;
		this.amount = amount;
		this.btcAddress = btcAddress;
	}
	
	public String getBtcAddress() {
		return btcAddress;
	}
	
	public void setBtcAddress(String btcAddress) {
		this.btcAddress = btcAddress;
	}

	@Override
	public String toString() {
		DecimalFormat DisplayFormatBTC = new DecimalFormat("#.########");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss", Locale.getDefault());
		sdf.setTimeZone(TimeZone.getDefault());
		
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(getTimestamp()));
		sb.append("\n");
		sb.append("PayOut Transaction: ");
		sb.append(DisplayFormatBTC.format(getAmount()));
		sb.append(" BTC to address: ");
		sb.append(getBtcAddress());
		return sb.toString();
	}
	
	public void encode(JSONObject o) {
		super.encode(o);
		if(btcAddress!=null) {
			o.put("btcAddress", btcAddress);
		}
    }

	public void decode(JSONObject o) {
		super.decode(o);
		setBtcAddress(TransferObject.toStringOrNull(o.get("btcAddress")));
    }
	
}
