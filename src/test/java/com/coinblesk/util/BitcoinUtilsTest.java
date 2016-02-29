/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import java.util.Arrays;
import java.util.List;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.TransactionOutput;
import org.bitcoinj.params.UnitTestParams;
import org.bitcoinj.testing.FakeTxBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author draft
 */
public class BitcoinUtilsTest {
    
    @Test
    public void sortTest() {
        Transaction tx = FakeTxBuilder.createFakeTx(UnitTestParams.get(), Coin.COIN, new ECKey());
        TransactionOutput to1 = tx.addOutput(Coin.valueOf(1), new ECKey());
        TransactionOutput to2 = tx.addOutput(Coin.valueOf(2), new ECKey());
        TransactionOutput to3 = tx.addOutput(Coin.valueOf(3), new ECKey());
        
        List<TransactionOutput> txs1 = Arrays.asList(to1, to2, to3);
        List<TransactionOutput> txs2 = Arrays.asList(to2, to1, to3);
        List<TransactionOutput> txs3 = Arrays.asList(to3, to2, to1);
        List<TransactionOutput> txs4 = Arrays.asList(to3, to1, to2);
        List<TransactionOutput> txs5 = Arrays.asList(to1, to3, to2);
        List<TransactionOutput> txs6 = Arrays.asList(to2, to3, to1);
        
        List<TransactionOutput> reference = BitcoinUtils.sort(txs1);
        Assert.assertEquals(reference, BitcoinUtils.sort(txs1));
        Assert.assertEquals(reference, BitcoinUtils.sort(txs2));
        Assert.assertEquals(reference, BitcoinUtils.sort(txs3));
        Assert.assertEquals(reference, BitcoinUtils.sort(txs4));
        Assert.assertEquals(reference, BitcoinUtils.sort(txs5));
        Assert.assertEquals(reference, BitcoinUtils.sort(txs6));
        Assert.assertNotEquals(txs1, txs2);
        
    }
}
