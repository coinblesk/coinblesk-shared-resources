/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.coinblesk.util;

import java.util.BitSet;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

public class BloomFilterTest {

    final private static byte[][] TEST_DATA = new byte[1000][32];

    static {
        Random r = new Random(42);
        for (byte[] t : TEST_DATA) {
            for (int i = 0; i < t.length; i++) {
                t[i] = (byte) r.nextInt();
            }
        }
    }
    
    @Test
    public void testBloomfilterSerialization() {
        SimpleBloomFilter<byte[]> sampleBf1 = new SimpleBloomFilter<>(0.001, 10);
        sampleBf1.add(TEST_DATA[10]);
        sampleBf1.add(TEST_DATA[22]);
        BitSet compare1 = sampleBf1.getBitSet();
        byte[] tmp = sampleBf1.encode();
        SimpleBloomFilter<byte[]> sampleBf2 = new SimpleBloomFilter<>(tmp);
        BitSet compare2 = sampleBf2.getBitSet();
        Assert.assertEquals(compare1, compare2);
        Assert.assertTrue(sampleBf2.contains(TEST_DATA[10]));
        Assert.assertTrue(sampleBf2.contains(TEST_DATA[22]));
        Assert.assertFalse(sampleBf2.contains(TEST_DATA[11]));
    }
    
    @Test
    public void testBloomfilterSerializationFull() {
        SimpleBloomFilter<byte[]> sampleBf1 = new SimpleBloomFilter<>(0.001, 100);
        for(int i=0;i<500;i++) {
            sampleBf1.add(TEST_DATA[i]);
        }
        BitSet compare1 = sampleBf1.getBitSet();
        byte[] tmp = sampleBf1.encode();
        SimpleBloomFilter<byte[]> sampleBf2 = new SimpleBloomFilter<>(tmp);
        BitSet compare2 = sampleBf2.getBitSet();
        Assert.assertEquals(compare1, compare2);
        Assert.assertTrue(sampleBf2.contains(TEST_DATA[10]));
        Assert.assertTrue(sampleBf2.contains(TEST_DATA[22]));
        Assert.assertFalse(sampleBf2.contains(TEST_DATA[601]));
        Assert.assertTrue(sampleBf2.contains(TEST_DATA[600])); //false positev, we have way too much items
    }


    @Test
    public void testBloomfilter() {
        SimpleBloomFilter<byte[]> sampleBf1 = new SimpleBloomFilter<>(0.001, 10);
        sampleBf1.add(TEST_DATA[0]);
        sampleBf1.add(TEST_DATA[2]);
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[0]));
        Assert.assertFalse(sampleBf1.contains(TEST_DATA[1]));
        Assert.assertEquals(25, sampleBf1.encode().length);
    }
    
    @Test
    public void testBloomfilterSimilar() {
        SimpleBloomFilter<byte[]> sampleBf1 = new SimpleBloomFilter<>(0.01, 1);
        sampleBf1.add(TEST_DATA[0]);
        byte[] copy = TEST_DATA[0].clone();
        copy[0]=1;
        sampleBf1.add(copy);
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[0]));
        Assert.assertTrue(sampleBf1.contains(copy));
        Assert.assertFalse(sampleBf1.contains(TEST_DATA[1]));
        Assert.assertEquals(8, sampleBf1.encode().length);
    }

    @Test
    public void testBloomfilterFalsePositive() {
        SimpleBloomFilter<byte[]> sampleBf1 = new SimpleBloomFilter<>(0.001, 1);
        sampleBf1.add(TEST_DATA[0]);
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[0]));
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[204]));
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[232]));
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[513]));
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[536]));
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[805]));
        Assert.assertTrue(sampleBf1.contains(TEST_DATA[809]));
        for (int i = 1; i < 999; i++) {
            //false positives
            if (i == 204 || i == 232 || i == 513 || i == 536 || i == 805 || i == 809) {
                continue;
            }
            Assert.assertFalse(sampleBf1.contains(TEST_DATA[i]));
        }
        Assert.assertEquals(8, sampleBf1.encode().length);
    }

}
