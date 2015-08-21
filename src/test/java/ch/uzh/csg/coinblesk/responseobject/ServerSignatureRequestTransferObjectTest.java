package ch.uzh.csg.coinblesk.responseobject;

import java.util.Arrays;
import java.util.Random;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import ch.uzh.csg.coinblesk.JsonConverter;

public class ServerSignatureRequestTransferObjectTest {
    
    private final static Random RND = new Random(42L);

    @Test
    public void testEncodeDecode() throws Exception {
        String msg = "message";
        String tx = "tx";
        int childNumber = RND.nextInt();
        
        ServerSignatureRequestTransferObject obj = new ServerSignatureRequestTransferObject();
        
        obj.setMessage(msg);
        obj.setPartialTx(tx);
        obj.addChildNumber(childNumber);
        
        String json = obj.toJson();
        System.out.println(json);
        
        ServerSignatureRequestTransferObject decoded = JsonConverter.fromJson(json, ServerSignatureRequestTransferObject.class);
        
        Assert.assertEquals(msg, decoded.getMessage());
        Assert.assertEquals(tx, decoded.getPartialTx());
        Assert.assertEquals(childNumber, (int) decoded.getChildNumbers().get(0));
        
        
    }

}
