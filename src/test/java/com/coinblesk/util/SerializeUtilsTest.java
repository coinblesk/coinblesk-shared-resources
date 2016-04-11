/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import com.coinblesk.json.SignTO;
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
        Assert.assertEquals(SerializeUtils.canonicalizeJSONHash(json1), SerializeUtils.canonicalizeJSONHash(json2));
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
        SerializeUtils.sign(p, client);
        Assert.assertTrue(SerializeUtils.verifySig(p, client));
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
        SerializeUtils.sign(p, client);
        SignTO p2 = SerializeUtils.GSON.fromJson(SerializeUtils.GSON.toJson(p),
                SignTO.class);
        Assert.assertTrue(SerializeUtils.verifySig(p2, client));
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
        SerializeUtils.sign(p, client);
        String stream = SerializeUtils.GSON.toJson(p);
        stream = stream.replace("3", "4");
        SignTO p2 = SerializeUtils.GSON.fromJson(stream, SignTO.class);
        Assert.assertFalse(SerializeUtils.verifySig(p2, client));
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
        SerializeUtils.sign(p, client);
        String stream = SerializeUtils.GSON.toJson(p);
        //Attention the resolution is on a second basis!
        SignTO p2 = SerializeUtils.GSON.fromJson(stream, SignTO.class);
        p2.currentDate(1);
        Assert.assertFalse(SerializeUtils.verifySig(p2, client));
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
        SerializeUtils.sign(p, client);
        Assert.assertFalse(SerializeUtils.verifySig(p, server));
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
        SerializeUtils.sign(p, client);
        p.amountToSpend(4);
        System.err.println(SerializeUtils.GSON.toJson(p));
        Assert.assertFalse(SerializeUtils.verifySig(p, server));
    }
}
