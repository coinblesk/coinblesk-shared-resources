package ch.uzh.csg.coinblesk.util;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.coinblesk.responseobject.UserAccountObject;
import ch.uzh.csg.coinblesk.responseobject.TransferObject.Status;

public class JSONConvertUserTest {
	@Test
	public void testUserAccountObject1() throws Exception {
		UserAccountObject u = new UserAccountObject();
		UserAccountObject u2 = encodeDecode(u);
		Assert.assertNull(u2.getId());
		Assert.assertNull(u2.getUsername());
		Assert.assertNull(u2.getEmail());
		Assert.assertNull(u2.getPassword());
		Assert.assertNull(u2.getPaymentAddress());
		Assert.assertNull(u2.getBalanceBTC());
		Assert.assertEquals(1, u2.getVersion());
		Assert.assertEquals(Status.REQUEST, u2.getStatus());
	}
	
	@Test
	public void testUserAccountObject2() throws Exception {
		UserAccountObject u = new UserAccountObject();
		u.setUsername("tom");
		u.setEmail("tom@");
		u.setPassword("jup");
		u.setId(12L);
		u.setBalanceBTC(new BigDecimal("0.000000009"));
		u.setPaymentAddress("123");
		UserAccountObject u2 = encodeDecode(u);
		Assert.assertEquals(Long.valueOf(12L),u2.getId());
		Assert.assertEquals("tom",u2.getUsername());
		Assert.assertEquals("tom@",u2.getEmail());
		Assert.assertEquals("jup",u2.getPassword());
		Assert.assertEquals("123",u2.getPaymentAddress());
		Assert.assertEquals(new BigDecimal("0.00000001"), u2.getBalanceBTC());
	}
	
	private UserAccountObject encodeDecode(UserAccountObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		UserAccountObject output = new UserAccountObject();
		output.decode(encoded);
		return output;
	}
}
