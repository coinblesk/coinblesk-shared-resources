package ch.uzh.csg.coinblesk.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Random;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.coinblesk.responseobject.TransactionObject;

public class JSONConvertTransactionTest {
	
	@Test
	public void testTransaction1() throws Exception {
		TransactionObject t = new TransactionObject();
		t.setBalanceBTC(new BigDecimal("20999999.97690000"));
		byte[] b = new byte[200];
		new Random().nextBytes(b);
		t.setServerPaymentResponse(b);
		TransactionObject t2 = encodeDecode(t);
		Assert.assertTrue(Arrays.equals(b, t2.getServerPaymentResponse()));
		Assert.assertEquals(new BigDecimal("20999999.97690000"), t2.getBalanceBTC());
	}
	
	private TransactionObject encodeDecode(TransactionObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		TransactionObject output = new TransactionObject();
		output.decode(encoded);
		return output;
	}
}
