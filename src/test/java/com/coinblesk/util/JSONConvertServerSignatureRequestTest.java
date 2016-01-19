package com.coinblesk.util;

import java.util.Random;

import net.minidev.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import com.coinblesk.responseobject.ServerSignatureRequestTransferObject;

public class JSONConvertServerSignatureRequestTest {
    
    private final static Random RND = new Random(42L);

    @Test
    public void testServerSignatureRequestObject() throws Exception {
        
//        ServerSignatureRequestTransferObject originalObject = new ServerSignatureRequestTransferObject();
//        String mockTx = "360436fc72f550e7a63da99250004af9997e6fd71c21f5588d71a1f69b4f4a31";
//        originalObject.setPartialTx(mockTx);
//        
//        for(int i = 0; i < 5; i++) {
//            int index = RND.nextInt();
//            int[] path = {RND.nextInt(), RND.nextInt(), RND.nextInt(), RND.nextInt(), };
//            originalObject.addIndexAndDerivationPath(index, path);
//        }
//        
//        JSONObject originalEncoded = new JSONObject();
//        originalObject.encode(originalEncoded);
//        
//        ServerSignatureRequestTransferObject restoredObject = new ServerSignatureRequestTransferObject();
//        restoredObject.decode(originalEncoded.toJSONString());
//        
//        Assert.assertEquals(originalObject.getPartialTx(), restoredObject.getPartialTx());
//        
//        Assert.assertEquals(originalObject.getIndexAndDerivationPaths().size(), restoredObject.getIndexAndDerivationPaths().size());
//        for(int i = 0; i < originalObject.getIndexAndDerivationPaths().size(); i++) {
//            Assert.assertArrayEquals(originalObject.getIndexAndDerivationPaths().get(i).getDerivationPath(), 
//                    restoredObject.getIndexAndDerivationPaths().get(i).getDerivationPath());
//            Assert.assertEquals(originalObject.getIndexAndDerivationPaths().get(i).getIndex(), 
//                    restoredObject.getIndexAndDerivationPaths().get(i).getIndex());
//        }
        
    }

}
