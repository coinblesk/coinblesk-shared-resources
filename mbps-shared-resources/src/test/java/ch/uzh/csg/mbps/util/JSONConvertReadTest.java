package ch.uzh.csg.mbps.util;

import java.math.BigDecimal;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.mbps.keys.CustomPublicKey;
import ch.uzh.csg.mbps.responseobject.CustomPublicKeyObject;
import ch.uzh.csg.mbps.responseobject.ReadRequestObject;
import ch.uzh.csg.mbps.responseobject.UserAccountObject;

public class JSONConvertReadTest {
	
	@Test
	public void testReadRequestObject1() throws Exception {
		ReadRequestObject r = new ReadRequestObject();
		
		UserAccountObject u = new UserAccountObject();
		u.setBalanceBTC(new BigDecimal("20999999.97690000"));
		u.setId(11L);
		u.setUsername("tom");
		r.setUserAccount(u);
		
		CustomPublicKeyObject c = new CustomPublicKeyObject();
		CustomPublicKey cp = new CustomPublicKey();
		cp.setKeyNumber((byte)8);
		cp.setPkiAlgorithm((byte)5);
		cp.setPublicKey("this is my public key");
		
		c.setCustomPublicKey(cp);
		r.setCustomPublicKey(c);
		
		ReadRequestObject r2 = encodeDecode(r);
		
		UserAccountObject u2 = r2.getUserAccount();
		CustomPublicKeyObject c2 = r2.getCustomPublicKey();
		
		Assert.assertEquals(new BigDecimal("20999999.97690000"), u2.getBalanceBTC());
		Assert.assertEquals("this is my public key", c2.getCustomPublicKey().getPublicKey());
		Assert.assertEquals(8, c2.getCustomPublicKey().getKeyNumber());
		Assert.assertEquals(5, c2.getCustomPublicKey().getPkiAlgorithm());
		
	}
	
	private ReadRequestObject encodeDecode(ReadRequestObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		ReadRequestObject output = new ReadRequestObject();
		output.decode(encoded);
		return output;
	}
}
