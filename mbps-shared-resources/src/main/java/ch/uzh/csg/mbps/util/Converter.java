package ch.uzh.csg.mbps.util;

import java.math.BigDecimal;

//TODO: javadoc
public class Converter {
	
	/**
	 * Converts a long value back to its corresponding BigDecimal value by dividing.
	 * 
	 * @param longValue
	 * @return BigDecimal
	 */
	public static BigDecimal getBigDecimalFromLong(long longValue){
		BigDecimal bigDecimalValue = new BigDecimal(longValue);
		BigDecimal result = bigDecimalValue.divide(new BigDecimal(100000000));
		return result;
	}
	
	/**
	 * Converts a BigDecimal value with 8 decimal places into a long value by multiplying.
	 *  
	 * @param bigDecimalValue
	 * @return bigDecimal as long
	 */
	public static long getLongFromBigDecimal(BigDecimal bigDecimalValue){
		BigDecimal result = bigDecimalValue.multiply(new BigDecimal(100000000));
		return result.longValue();
	}

}
