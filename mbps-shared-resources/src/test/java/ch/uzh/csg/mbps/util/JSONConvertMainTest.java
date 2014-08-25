package ch.uzh.csg.mbps.util;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.mbps.responseobject.MainRequestObject;
import ch.uzh.csg.mbps.responseobject.UserAccountObject;

public class JSONConvertMainTest {
	@Test
	public void testPayOutTransactionObject1() throws Exception {
		MainRequestObject m = new MainRequestObject();

		m.setGetHistoryTransferObject(JSONConvertHistoryResponseTest.fillHistory());

		m.setExchangeRate(new BigDecimal("11.11"), "USD");

		UserAccountObject u = new UserAccountObject();
		u.setUsername("tom");
		u.setEmail("tom@");
		u.setPassword("jup");
		u.setId(12L);

		m.setUserAccount(u);

		MainRequestObject m2 = encodeDecode(m);

		Assert.assertEquals("jup", m2.getUserAccount().getPassword());
		Assert.assertEquals("USD", m2.getOtherCurrency());
		Assert.assertEquals(new BigDecimal("11.11"), m2.getExchangeRate());
	}

	private MainRequestObject encodeDecode(MainRequestObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		MainRequestObject output = new MainRequestObject();
		output.decode(encoded);
		return output;
	}
}
