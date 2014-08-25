package ch.uzh.csg.mbps.util;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.mbps.responseobject.PayOutTransactionObject;

public class JSONConvertPayoutRuleTest {
	@Test
	public void testPayOutTransactionObject1() throws Exception {
		PayOutTransactionObject p = new PayOutTransactionObject();
		
		p.setAmount(new BigDecimal("0.00010000"));
		p.setBtcAddress("this is a btc address");
		
		PayOutTransactionObject p2 = encodeDecode(p);
		
		Assert.assertEquals(new BigDecimal("0.00010000"), p2.getAmount());
		Assert.assertEquals("this is a btc address", p2.getBtcAddress());
	}
	
	private PayOutTransactionObject encodeDecode(PayOutTransactionObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		PayOutTransactionObject output = new PayOutTransactionObject();
		output.decode(encoded);
		return output;
	}
}
