package com.coinblesk.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class offers the functionality to convert BTC amounts in BigDecimal
 * representation to longs and vice versa.
 */
public class Converter {
	
	/**
	 * Converts a long value back to its corresponding BigDecimal (used to
	 * convert a long representing a BTC amount back to a BigDecimal with 8
	 * decimal places).
	 */
	public static BigDecimal getBigDecimalFromLong(long longValue){
		BigDecimal bigDecimalValue = new BigDecimal(longValue);
		BigDecimal result = bigDecimalValue.divide(new BigDecimal(100000000));
		return result.setScale(8, RoundingMode.HALF_UP);
	}
	
	/**
	 * Converts a BigDecimal value with 8 decimal places into a long (used to
	 * represent a BTC amount in 1 long).
	 */
	public static long getLongFromBigDecimal(BigDecimal bigDecimalValue){
		BigDecimal result = bigDecimalValue.multiply(new BigDecimal(100000000));
		return result.setScale(8, RoundingMode.HALF_UP).longValue();
	}
	
	public static BigDecimal getBigDecimalFromString(String stringValue) {
		BigDecimal result = new BigDecimal(stringValue);
		return result.setScale(8, RoundingMode.HALF_UP);
	}

}
