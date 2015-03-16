package ch.uzh.csg.coinblesk.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.coinblesk.model.HistoryPayInTransaction;
import ch.uzh.csg.coinblesk.model.HistoryPayOutTransaction;
import ch.uzh.csg.coinblesk.model.HistoryTransaction;
import ch.uzh.csg.coinblesk.responseobject.GetHistoryTransferObject;

public class JSONConvertHistoryResponseTest {
	
	private static Date now;
	private static Date now6;
	
	@Test
	public void testPayOutTransactionObject1() throws Exception {
		
		GetHistoryTransferObject g2 = encodeDecode(fillHistory());
		
		Assert.assertEquals(Long.valueOf(11), g2.getNofPayInTransactions());
		Assert.assertEquals(Long.valueOf(22), g2.getNofPayOutTransactions());
		Assert.assertEquals(Long.valueOf(33), g2.getNofTransactions());
		
		Assert.assertEquals(2, g2.getPayInTransactionHistory().size());
		Assert.assertEquals(2, g2.getPayOutTransactionHistory().size());
		Assert.assertEquals(2, g2.getTransactionHistory().size());
		
		Assert.assertEquals(now, g2.getTransactionHistory().get(0).getTimestamp());
		Assert.assertEquals(now6, g2.getPayOutTransactionHistory().get(1).getTimestamp());
		
		Assert.assertEquals("CHF2", g2.getTransactionHistory().get(1).getInputCurrency());
		Assert.assertEquals(new BigDecimal("3.2"), g2.getTransactionHistory().get(1).getInputCurrencyAmount());
		
		
	}
	
	public static GetHistoryTransferObject fillHistory() throws InterruptedException {
		GetHistoryTransferObject g = new GetHistoryTransferObject();
		List<HistoryTransaction> l1 = new ArrayList<HistoryTransaction>();
		HistoryTransaction ht1 = new HistoryTransaction();
		ht1.setAmount(new BigDecimal("2.0"));
		ht1.setBuyer("buyer");
		ht1.setInputCurrency("CHF");
		ht1.setInputCurrencyAmount(new BigDecimal("3.0"));
		ht1.setSeller("seller");
		now = Calendar.getInstance().getTime();
		ht1.setTimestamp(now);
		l1.add(ht1);
		//
		HistoryTransaction ht2 = new HistoryTransaction();
		ht2.setAmount(new BigDecimal("3.0"));
		ht2.setBuyer("buyer2");
		ht2.setInputCurrency("CHF2");
		ht2.setInputCurrencyAmount(new BigDecimal("3.2"));
		ht2.setSeller("seller2");
		Date now2 = Calendar.getInstance().getTime();
		ht2.setTimestamp(now2);
		l1.add(ht2);
		g.setTransactionHistory(l1);
		//
		//
		List<HistoryPayInTransaction> l2 = new ArrayList<HistoryPayInTransaction>();
		HistoryPayInTransaction hp1 = new HistoryPayInTransaction();
		hp1.setAmount(new BigDecimal("33.0"));
		Date now3= Calendar.getInstance().getTime();
		hp1.setTimestamp(now3);
		l2.add(hp1);
		HistoryPayInTransaction hp2 = new HistoryPayInTransaction();
		hp2.setAmount(new BigDecimal("34.0"));
		Date now4= Calendar.getInstance().getTime();
		hp2.setTimestamp(now4);
		l2.add(hp2);
		g.setPayInTransactionHistory(l2);
		//
		//
		List<HistoryPayOutTransaction> l3 = new ArrayList<HistoryPayOutTransaction>();
		HistoryPayOutTransaction ho1 = new HistoryPayOutTransaction();
		ho1.setAmount(new BigDecimal("35.0"));
		ho1.setBtcAddress("btc addr344");
		Date now5= Calendar.getInstance().getTime();
		ho1.setTimestamp(now5);
		l3.add(ho1);
		HistoryPayOutTransaction ho2 = new HistoryPayOutTransaction();
		ho2.setAmount(new BigDecimal("35.2"));
		ho2.setBtcAddress("btc addr5");
		Thread.sleep(50);
		now6= Calendar.getInstance().getTime();
		ho2.setTimestamp(now6);
		l3.add(ho2);
		g.setPayOutTransactionHistory(l3);
		g.setNofPayInTransactions(11L);
		g.setNofPayOutTransactions(22L);
		g.setNofTransactions(33L);
		return g;
	}
	
	
	
	private GetHistoryTransferObject encodeDecode(GetHistoryTransferObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		GetHistoryTransferObject output = new GetHistoryTransferObject();
		output.decode(encoded);
		return output;
	}
}
