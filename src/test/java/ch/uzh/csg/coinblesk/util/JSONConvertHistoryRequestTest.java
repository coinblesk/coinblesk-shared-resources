package ch.uzh.csg.coinblesk.util;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.coinblesk.responseobject.HistoryTransferRequestObject;

public class JSONConvertHistoryRequestTest {
	@Test
	public void testPayOutTransactionObject1() throws Exception {
		HistoryTransferRequestObject h = new HistoryTransferRequestObject();
		h.setTxPage(11);
		h.setTxPayInPage(22);
		h.setTxPayInUnverifiedPage(33);
		h.setTxPayOutPage(44);
		
		
		HistoryTransferRequestObject h2 = encodeDecode(h);
		
		Assert.assertEquals(Integer.valueOf(11), h2.getTxPage());
        Assert.assertEquals(Integer.valueOf(22), h2.getTxPayInPage());
        Assert.assertEquals(Integer.valueOf(33), h2.getTxPayInUnverifiedPage());
		Assert.assertEquals(Integer.valueOf(44), h2.getTxPayOutPage());
		
		
	}
	
	private HistoryTransferRequestObject encodeDecode(HistoryTransferRequestObject input) throws Exception {
		JSONObject jsonObject = new JSONObject();
		input.encode(jsonObject);
		String encoded = jsonObject.toJSONString();
		System.err.println("transfer:" + encoded);
		HistoryTransferRequestObject output = new HistoryTransferRequestObject();
		output.decode(encoded);
		return output;
	}
}
