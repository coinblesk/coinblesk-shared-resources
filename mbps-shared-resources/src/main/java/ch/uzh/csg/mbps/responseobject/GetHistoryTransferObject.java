package ch.uzh.csg.mbps.responseobject;

import java.util.ArrayList;

import ch.uzh.csg.mbps.model.HistoryPayInTransaction;
import ch.uzh.csg.mbps.model.HistoryPayOutTransaction;
import ch.uzh.csg.mbps.model.HistoryTransaction;

public class GetHistoryTransferObject {
	private ArrayList<HistoryTransaction> transactionHistory;
	private ArrayList<HistoryPayInTransaction> payInTransactionHistory;
	private ArrayList<HistoryPayOutTransaction> payOutTransactionHistory;
	
	private long nofTransactions;
	private long nofPayInTransactions;
	private long nofPayOutTransactions;
	
	public GetHistoryTransferObject() {
	}
	
	public GetHistoryTransferObject(ArrayList<HistoryTransaction> transactions,
			ArrayList<HistoryPayInTransaction> payInTransactions,
			ArrayList<HistoryPayOutTransaction> payOutTransactions,
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

	public ArrayList<HistoryTransaction> getTransactionHistory() {
		return transactionHistory;
	}

	public void setTransactionHistory(ArrayList<HistoryTransaction> transactionHistory) {
		this.transactionHistory = transactionHistory;
	}

	public ArrayList<HistoryPayInTransaction> getPayInTransactionHistory() {
		return payInTransactionHistory;
	}

	public void setPayInTransactionHistory(ArrayList<HistoryPayInTransaction> payInTransactionHistory) {
		this.payInTransactionHistory = payInTransactionHistory;
	}

	public ArrayList<HistoryPayOutTransaction> getPayOutTransactionHistory() {
		return payOutTransactionHistory;
	}

	public void setPayOutTransactionHistory(ArrayList<HistoryPayOutTransaction> payOutTransactionHistory) {
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

}
