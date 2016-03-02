/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import com.coinblesk.json.PrepareHalfSignTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
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
    public void testSignatureOk() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        PrepareHalfSignTO p = new PrepareHalfSignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(new Date());
        SerializeUtils.sign(p, client);
        Assert.assertTrue(SerializeUtils.verifySig(p, client));
    }
    
    @Test
    public void testSignatureOkSerialize() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        PrepareHalfSignTO p = new PrepareHalfSignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(new Date());
        SerializeUtils.sign(p, client);
        PrepareHalfSignTO p2 = SerializeUtils.GSON.fromJson(SerializeUtils.GSON.toJson(p), PrepareHalfSignTO.class);
        Assert.assertTrue(SerializeUtils.verifySig(p2, client));
    }
    
    @Test
    public void testSignatureNokSerialize() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        PrepareHalfSignTO p = new PrepareHalfSignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(new Date());
        SerializeUtils.sign(p, client);
        String stream = SerializeUtils.GSON.toJson(p);
        stream = stream.replace("3", "4");
        PrepareHalfSignTO p2 = SerializeUtils.GSON.fromJson(stream, PrepareHalfSignTO.class);
        Assert.assertFalse(SerializeUtils.verifySig(p2, client));
    }
    
    @Test
    public void testSignatureNokSerialize2() throws InterruptedException {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        PrepareHalfSignTO p = new PrepareHalfSignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(new Date());
        SerializeUtils.sign(p, client);
        String stream = SerializeUtils.GSON.toJson(p);
        //Attention the resolution is on a second basis!
        PrepareHalfSignTO p2 = SerializeUtils.GSON.fromJson(stream, PrepareHalfSignTO.class);
        p2.currentDate(new Date(1));
        Assert.assertFalse(SerializeUtils.verifySig(p2, client));
    }
    
    @Test
    public void testSignatureRebuild() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        PrepareHalfSignTO p = new PrepareHalfSignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                .currentDate(new Date());
        SerializeUtils.sign(p, client);
        Assert.assertFalse(SerializeUtils.verifySig(p, server));
    }
    
    @Test
    public void testSignatureChangedMessage() {
        ECKey client = new ECKey();
        ECKey server = new ECKey();
        PrepareHalfSignTO p = new PrepareHalfSignTO()
                .amountToSpend(3)
                .clientPublicKey(client.getPubKey())
                .p2shAddressTo(server.toAddress(UnitTestParams.get()).toString())
                
                .currentDate(new Date());
        SerializeUtils.sign(p, client);
        p.amountToSpend(4);
        System.err.println(SerializeUtils.GSON.toJson(p));
        Assert.assertFalse(SerializeUtils.verifySig(p, server));
    }
}
