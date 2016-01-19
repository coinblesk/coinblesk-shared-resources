package com.coinblesk.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;

import com.coinblesk.util.Converter;

public class ConverterTest2 {
	
	@Test
	public void testGetLongFromString() {
		BigDecimal orig = new BigDecimal("20999999.97690000");
		BigDecimal conv =  Converter.getBigDecimalFromString("20999999.97690000");
		assertEquals(orig, conv);
		BigDecimal conv1 =  Converter.getBigDecimalFromString("20999999.976900001");
		assertEquals(orig, conv1);
		BigDecimal conv2 =  Converter.getBigDecimalFromString("20999999.976899999");
		assertEquals(orig, conv2);
	}

	@Test
	public void testGetBigDecimalFromLong() {
		// 1000000 = 0.01 btc
		long l = 1000000;
		BigDecimal bigDecimalFromLong = Converter.getBigDecimalFromLong(l);
		assertEquals("0.01000000", bigDecimalFromLong.toString());
		
		// 100000000 = 1 btc
		l = 100000000;
		bigDecimalFromLong = Converter.getBigDecimalFromLong(l);
		assertEquals("1.00000000", bigDecimalFromLong.toString());
		
		// 1 = 0.00000001 btc
		l = 1;
		bigDecimalFromLong = Converter.getBigDecimalFromLong(l);
		assertEquals("1E-8", bigDecimalFromLong.toString());
		
		// 10000000000000001 = 100000000.00000001 btc
		l = 10000000000000001L;
		bigDecimalFromLong = Converter.getBigDecimalFromLong(l);
		assertEquals("100000000.00000001", bigDecimalFromLong.toString());
	}
	
	@Test
	public void testGetLongFromBigDecimal() {
		// 0.01 btc = 1000000
		BigDecimal bd = new BigDecimal("0.01000000");
		long longFromBigDecimal = Converter.getLongFromBigDecimal(bd);
		assertEquals(1000000, longFromBigDecimal);

		// 1 btc = 100000000
		bd = new BigDecimal("1.00000000");
		longFromBigDecimal = Converter.getLongFromBigDecimal(bd);
		assertEquals(100000000, longFromBigDecimal);
		
		// 0.00000001 btc = 1 satoshi -> this is the minimum
		bd = new BigDecimal("0.00000001");
		longFromBigDecimal = Converter.getLongFromBigDecimal(bd);
		assertEquals(1, longFromBigDecimal);
		
		// 100000000.00000001 btc = 10000000000000001
		bd = new BigDecimal("100000000.00000001");
		longFromBigDecimal = Converter.getLongFromBigDecimal(bd);
		assertEquals(10000000000000001L, longFromBigDecimal);
		
		//https://en.bitcoin.it/wiki/Bitcoin
		//maximum 2,099,999,997,690,000 satoshis
		bd = new BigDecimal("20999999.97690000"); // maxvalue
		longFromBigDecimal = Converter.getLongFromBigDecimal(bd);
		assertEquals(2099999997690000L, longFromBigDecimal);
		BigDecimal bd2 = Converter.getBigDecimalFromLong(longFromBigDecimal);
		assertEquals(bd, bd2);
	}
	
	
}
