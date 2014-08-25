package ch.uzh.csg.mbps.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.mbps.model.PayOutRule;
import ch.uzh.csg.mbps.responseobject.PayOutRulesTransferObject;

public class JSONConvertPayoutTest {
	@Test
	public void testPayOutTransactionObject1() throws Exception {
		PayOutRulesTransferObject p = new PayOutRulesTransferObject();
		
		PayOutRule pr1 = new PayOutRule(12L, 13, 14, "btc addr1");
		PayOutRule pr2 = new PayOutRule(15L, new BigDecimal("1"), "btc addr2");
		
		List<PayOutRule> l = new ArrayList<PayOutRule>();
		l.add(pr1);
		l.add(pr2);
		
		p.setPayOutRulesList(l);
		
		PayOutRulesTransferObject p2 = encodeDecode(p);
		
		Assert.assertEquals(2, p2.getPayOutRulesList().size());
		PayOutRule pr11 = p2.getPayOutRulesList().get(0);
		PayOutRule pr22 = p2.getPayOutRulesList().get(1);
		
		Assert.assertEquals(null, pr11.getUserId());
		Assert.assertEquals(null, pr22.getUserId());
		
		Assert.assertEquals(Integer.valueOf(13), pr11.getHour());
		Assert.assertEquals(Integer.valueOf(14), pr11.getDay());
		
		Assert.assertEquals("btc addr1", pr11.getPayoutAddress());
		Assert.assertEquals("btc addr2", pr22.getPayoutAddress());
		
		Assert.assertEquals(new BigDecimal("1.00000000"), pr22.getBalanceLimitBTC());
		Assert.assertNull(pr11.getBalanceLimitBTC());
		
	}
	
	private PayOutRulesTransferObject encodeDecode(PayOutRulesTransferObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		PayOutRulesTransferObject output = new PayOutRulesTransferObject();
		output.decode(encoded);
		return output;
	}
}
