package ch.uzh.csg.mbps.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HistoryServerAccountTransaction extends AbstractHistory {
	private static final long serialVersionUID = -6411119193220293391L;

	public HistoryServerAccountTransaction(){
	}
	
	public HistoryServerAccountTransaction(Date timestamp, BigDecimal amount){
		this.timestamp = timestamp;
		this.amount = amount;
	}
	
	@Override
	public String toString() {
		DecimalFormat DisplayFormatBTC = new DecimalFormat("#.########");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy' 'HH:mm:ss", Locale.getDefault());
		sdf.setTimeZone(TimeZone.getDefault());
		
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(getTimestamp()));
		sb.append("\n");
		sb.append("ServerAccount Transaction from BTC Network: ");
		sb.append(DisplayFormatBTC.format(getAmount()));
		sb.append(" BTC");
		return sb.toString();
	}

}