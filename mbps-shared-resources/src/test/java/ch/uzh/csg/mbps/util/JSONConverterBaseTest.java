package ch.uzh.csg.mbps.util;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.mbps.responseobject.TransferObject;
import ch.uzh.csg.mbps.responseobject.TransferObject.Status;

public class JSONConverterBaseTest {
	
	@Test
	public void testTransferObject1() throws Exception {
		TransferObject to = new TransferObject();
		to.setMessage("hallo");
		to.setStatus(Status.REPLY_FAILED);
		to.setVersion(22);
		TransferObject to2 = encodeDecode(to);
		Assert.assertEquals("hallo", to2.getMessage());
		Assert.assertFalse(to2.isSuccessful());
		Assert.assertEquals(Status.REPLY_FAILED, to2.getStatus());
		Assert.assertEquals(22, to2.getVersion());
	}
	
	@Test
	public void testTransferObject2() throws Exception {
		TransferObject to = new TransferObject();
		to.setStatus(Status.REPLY_FAILED);
		to.setVersion(22);
		TransferObject to2 = encodeDecode(to);
		Assert.assertEquals(null, to2.getMessage());
		Assert.assertFalse(to2.isSuccessful());
		Assert.assertEquals(Status.REPLY_FAILED, to2.getStatus());
		Assert.assertEquals(22, to2.getVersion());
	}
	
	@Test
	public void testTransferObject3() throws Exception {
		TransferObject to = new TransferObject();
		to.setStatus(Status.REPLY_SUCCESS);
		TransferObject to2 = encodeDecode(to);
		Assert.assertEquals(null, to2.getMessage());
		Assert.assertTrue(to2.isSuccessful());
		Assert.assertEquals(Status.REPLY_SUCCESS, to2.getStatus());
		Assert.assertEquals(1, to2.getVersion());
	}
	
	private TransferObject encodeDecode(TransferObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		TransferObject output = new TransferObject();
		output.decode(encoded);
		return output;
	}
	
	
}
