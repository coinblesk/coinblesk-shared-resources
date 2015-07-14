package ch.uzh.csg.coinblesk.responseobject;

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
        int index = RND.nextInt();
        int[] paths = {1,2,3,4,5,6,7};
        
        ServerSignatureRequestTransferObject obj = new ServerSignatureRequestTransferObject();
        
        obj.setMessage(msg);
        obj.setPartialTx(tx);
        obj.addIndexAndDerivationPath(index, paths);
        
        String json = obj.toJson();
        System.out.println(json);
        
        ServerSignatureRequestTransferObject decoded = JsonConverter.fromJson(json, ServerSignatureRequestTransferObject.class);
        
        Assert.assertEquals(msg, decoded.getMessage());
        Assert.assertEquals(tx, decoded.getPartialTx());
        Assert.assertEquals(index, decoded.getIndexAndDerivationPaths().get(0).getIndex());
        Assert.assertArrayEquals(paths, decoded.getIndexAndDerivationPaths().get(0).getDerivationPath());
        
        
    }

}
