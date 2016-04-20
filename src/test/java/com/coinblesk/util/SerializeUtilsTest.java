/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import com.coinblesk.json.SignTO;
import com.coinblesk.json.TxSig;
import com.coinblesk.json.VerifyTO;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.params.UnitTestParams;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author draft
 */
public class SerializeUtilsTest {

    @Test
    public void testOrder() {
        String json1 = "{\n"
                + "  \"amountToSpend\": 3,\n"
                + "  \"p2shAddressTo\": \"msaR6WeixDg5CFTKy6pVKe6HbG8bFxDajZ\",\n"
                + "  \"type\": 0,\n"
                + "  \"messageSig\": {\n"
                + "    \"sigS\": \"31508828331232788577082964979495496547942777680132432034841436736860451836856\",\n"
                + "    \"sigR\": \"74907189571302380198686832665811666467813733025891418980987412059235732894027\"\n"
                + "  },\n"
                + "  \"currentDate\": 1457300122942,\n"
                + "  \"clientPublicKey\": \"Aw5QSWxGgUq7tdklwDY3a+DuqSQVWv0QUuCMo+ILwgeP\\r\\n\"\n"
                + "}";
        String json2 = "{\n"
                + "  \"p2shAddressTo\": \"msaR6WeixDg5CFTKy6pVKe6HbG8bFxDajZ\",\n"
                + "  \"type\": 0,\n"
                + "  \"messageSig\": {\n"
                + "    \"sigR\": \"74907189571302380198686832665811666467813733025891418980987412059235732894027\",\n"
                + "    \"sigS\": \"31508828331232788577082964979495496547942777680132432034841436736860451836856\"\n"
                + "  },\n"
                + "  \"currentDate\": 1457300122942,\n"
                + "  \"clientPublicKey\": \"Aw5QSWxGgUq7tdklwDY3a+DuqSQVWv0QUuCMo+ILwgeP\\r\\n\"\n"
                + "  \"amountToSpend\": 3\n"
                + "}";
        String cjson1 = SerializeUtils.canonicalizeJSON(json1);
        String cjson2 = SerializeUtils.canonicalizeJSON(json2);
        Assert.assertEquals(SerializeUtils.hash(cjson1), SerializeUtils.hash(cjson2));
    }

    @Test
    public void testSignatureOk() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        SignTO p = new SignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(System.currentTimeMillis());
        SerializeUtils.signJSON(p, client);
        Assert.assertTrue(SerializeUtils.verifyJSONSignature(p, client));
    }

    @Test
    public void testSignatureOkSerialize() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        SignTO p = new SignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(System.currentTimeMillis());
        SerializeUtils.signJSON(p, client);
        SignTO p2 = SerializeUtils.GSON.fromJson(SerializeUtils.GSON.toJson(p),
                SignTO.class);
        Assert.assertTrue(SerializeUtils.verifyJSONSignature(p2, client));
    }

    @Test
    public void testSignatureNokSerialize() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        SignTO p = new SignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(System.currentTimeMillis());
        SerializeUtils.signJSON(p, client);
        String stream = SerializeUtils.GSON.toJson(p);
        stream = stream.replace("3", "4");
        SignTO p2 = SerializeUtils.GSON.fromJson(stream, SignTO.class);
        Assert.assertFalse(SerializeUtils.verifyJSONSignature(p2, client));
    }

    @Test
    public void testSignatureNokSerialize2() throws InterruptedException {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        SignTO p = new SignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(System.currentTimeMillis());
        SerializeUtils.signJSON(p, client);
        String stream = SerializeUtils.GSON.toJson(p);
        //Attention the resolution is on a second basis!
        SignTO p2 = SerializeUtils.GSON.fromJson(stream, SignTO.class);
        p2.currentDate(1);
        Assert.assertFalse(SerializeUtils.verifyJSONSignature(p2, client));
    }

    @Test
    public void testSignatureRebuild() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        SignTO p = new SignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(System.currentTimeMillis());
        SerializeUtils.signJSON(p, client);
        Assert.assertFalse(SerializeUtils.verifyJSONSignature(p, server));
    }

    @Test
    public void testSignatureChangedMessage() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        SignTO p = new SignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(System.currentTimeMillis());
        SerializeUtils.signJSON(p, client);
        p.amountToSpend(4);
        System.err.println(SerializeUtils.GSON.toJson(p));
        Assert.assertFalse(SerializeUtils.verifyJSONSignature(p, server));
    }
    
    @Test
    public void testVerifyTO() {
        for(int i=0;i<100;i++) {
            testVerify();
        }
    }
    
    private void testVerify() {
         ECKey client = new ECKey();
        VerifyTO verifyTO= new VerifyTO();
        Random rnd = new Random(42L);
        verifyTO.amountToSpend(rnd.nextLong());
        byte[] outpoint = new byte[rnd.nextInt(1000)];
        rnd.nextBytes(outpoint);
        Pair<byte[], Long> pair = new Pair<>(outpoint, rnd.nextLong());
        List<Pair<byte[], Long>> list = new ArrayList<>();
        list.add(pair);
        verifyTO.outpointsCoinPair(list);
        byte[] clientPubKey = new byte[rnd.nextInt(1000)];
        rnd.nextBytes(clientPubKey);
        verifyTO.clientPublicKey(clientPubKey);
        List<TxSig> list1 = new ArrayList<>();
        int len1 = rnd.nextInt(100);
        for(int i=0;i<len1;i++) {
            TxSig ts = new TxSig();
            ts.sigR(new BigInteger(rnd.nextInt(1000), rnd).toString());
            ts.sigS(new BigInteger(rnd.nextInt(1000), rnd).toString());
            list1.add(ts);
        }
        
        int len2 = rnd.nextInt(100);
        List<TxSig> list2 = new ArrayList<>();
        for(int i=0;i<len2;i++) {
            TxSig ts = new TxSig();
            ts.sigR(new BigInteger(rnd.nextInt(1000), rnd).toString());
            ts.sigS(new BigInteger(rnd.nextInt(1000), rnd).toString());
            list2.add(ts);
        }
        verifyTO.clientSignatures(list1);
        verifyTO.serverSignatures(list2);
        
        SerializeUtils.signJSON(verifyTO, client);
        Assert.assertTrue(SerializeUtils.verifyJSONSignature(verifyTO, client));
    }
}
