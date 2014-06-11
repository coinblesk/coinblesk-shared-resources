package ch.uzh.csg.mbps.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConverterTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetBigDecimalFromLong() {
		// 1000000 = 0.01 btc
		long l = 1000000;
		BigDecimal bigDecimalFromLong = Converter.getBigDecimalFromLong(l);
		assertEquals("0.01", bigDecimalFromLong.toString());
		
		// 100000000 = 1 btc
		l = 100000000;
		bigDecimalFromLong = Converter.getBigDecimalFromLong(l);
		assertEquals("1", bigDecimalFromLong.toString());
		
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
		
		// 0.00000001 btc = 1
		bd = new BigDecimal("0.00000001");
		longFromBigDecimal = Converter.getLongFromBigDecimal(bd);
		assertEquals(1, longFromBigDecimal);
		
		// 100000000.00000001 btc = 10000000000000001
		bd = new BigDecimal("100000000.00000001");
		longFromBigDecimal = Converter.getLongFromBigDecimal(bd);
		assertEquals(10000000000000001L, longFromBigDecimal);
	}

}
