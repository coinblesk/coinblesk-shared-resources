package ch.uzh.csg.mbps.responseobject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import ch.uzh.csg.mbps.model.HistoryPayInTransaction;
import ch.uzh.csg.mbps.model.HistoryPayOutTransaction;
import ch.uzh.csg.mbps.model.HistoryTransaction;

public class GetHistoryTransferObject {
	
	//TODO: check if correct format, just picked one
	private final static SimpleDateFormat parserSDF=new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
	
	private List<HistoryTransaction> transactionHistory;
	private List<HistoryPayInTransaction> payInTransactionHistory;
	private List<HistoryPayOutTransaction> payOutTransactionHistory;
	
	private long nofTransactions;
	private long nofPayInTransactions;
	private long nofPayOutTransactions;
	
	public GetHistoryTransferObject() {
	}
	
	public GetHistoryTransferObject(List<HistoryTransaction> transactions,
			List<HistoryPayInTransaction> payInTransactions,
			List<HistoryPayOutTransaction> payOutTransactions,
			long nofTransactions,
			long nofPayInTransactions,
			long nofPayOutTransactions) {
		this.transactionHistory = transactions;
		this.payInTransactionHistory = payInTransactions;
		this.payOutTransactionHistory = payOutTransactions;
		this.nofTransactions = nofTransactions;
		this.nofPayInTransactions = nofPayInTransactions;
		this.nofPayOutTransactions = nofPayOutTransactions;
	}

	public List<HistoryTransaction> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(List<HistoryTransaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

	public List<HistoryPayInTransaction> getPayInTransactionHistory() {
		return payInTransactionHistory;
	}

	public void setPayInTransactionHistory(List<HistoryPayInTransaction> payInTransactionHistory) {
		this.payInTransactionHistory = payInTransactionHistory;
	}

	public List<HistoryPayOutTransaction> getPayOutTransactionHistory() {
		return payOutTransactionHistory;
	}

	public void setPayOutTransactionHistory(List<HistoryPayOutTransaction> payOutTransactionHistory) {
		this.payOutTransactionHistory = payOutTransactionHistory;
	}
	
	public long getNofTransactions() {
		return nofTransactions;
	}

	public void setNofTransactions(long nofTransactions) {
		this.nofTransactions = nofTransactions;
	}

	public long getNofPayInTransactions() {
		return nofPayInTransactions;
	}

	public void setNofPayInTransactions(long nofPayInTransactions) {
		this.nofPayInTransactions = nofPayInTransactions;
	}

	public long getNofPayOutTransactions() {
		return nofPayOutTransactions;
	}

	public void setNofPayOutTransactions(long nofPayOutTransactions) {
		this.nofPayOutTransactions = nofPayOutTransactions;
	}

	public void decode(String getHistoryTO) throws ParseException {
		if(getHistoryTO == null) {
			return;
		}
		JSONObject o = (JSONObject) JSONValue.parse(getHistoryTO);
		String nofTransactions = CustomResponseObject.toStringOrNull(o.get("nofTransactions"));
		String nofPayInTransactions = CustomResponseObject.toStringOrNull(o.get("nofPayInTransactions"));
		String nofPayOutTransactions = CustomResponseObject.toStringOrNull(o.get("nofPayOutTransactions"));
		
		try {
			setNofTransactions(Long.parseLong(nofTransactions));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			setNofPayInTransactions(Long.parseLong(nofPayInTransactions));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			setNofPayOutTransactions(Long.parseLong(nofPayOutTransactions));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		JSONArray array1 = (JSONArray) o.get("transactionHistory");
		ArrayList<HistoryTransaction> transactionHistory = new ArrayList<HistoryTransaction>();
		for(Object o2:array1) {
			JSONObject o3 = (JSONObject) o2;
			HistoryTransaction h1 = new HistoryTransaction();
			h1.setBuyer(CustomResponseObject.toStringOrNull(o3.get("buyer")));
			h1.setSeller(CustomResponseObject.toStringOrNull(o3.get("seller")));
			h1.setInputCurrency(CustomResponseObject.toStringOrNull(o3.get("inputCurrency")));
			h1.setInputCurrencyAmount(CustomResponseObject.toBigDecimalOrNull(o3.get("inputCurrencyAmount")));
			transactionHistory.add(h1);
		}
		setTransactionHistory(transactionHistory);
		
		JSONArray array2 = (JSONArray) o.get("payInTransactionHistory");
		ArrayList<HistoryPayInTransaction> payInTransactionHistory = new ArrayList<HistoryPayInTransaction>();
		for(Object o2:array2) {
			JSONObject o3 = (JSONObject) o2;
			HistoryPayInTransaction h1 = new HistoryPayInTransaction();
			h1.setAmount(CustomResponseObject.toBigDecimalOrNull(o3.get("amount")));
			h1.setTimestamp(parserSDF.parse(o3.get("timestamp").toString()));
			payInTransactionHistory.add(h1);
		}
		setPayInTransactionHistory(payInTransactionHistory);
		
		JSONArray array3 = (JSONArray) o.get("payOutTransactionHistory");
		ArrayList<HistoryPayOutTransaction> payOutTransactionHistory = new ArrayList<HistoryPayOutTransaction>();
		for(Object o2:array3) {
			JSONObject o3 = (JSONObject) o2;
			HistoryPayOutTransaction h1 = new HistoryPayOutTransaction();
			h1.setAmount(CustomResponseObject.toBigDecimalOrNull(o3.get("amount")));
			h1.setTimestamp(parserSDF.parse(o3.get("timestamp").toString()));
			h1.setBtcAddress(o3.get("btcAddress").toString());
			payOutTransactionHistory.add(h1);
		}
		setPayOutTransactionHistory(payOutTransactionHistory);
    }

}
