/**
 * 
 */
package com.sdcworld.utility;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * @author souro
 *
 */
public class ConvertUtility {
	
	private ConvertUtility()
	{		
	}
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	
	public static void setAnotherDateFormat(String format)
	{
		if (format != null)
			dateFormat = new SimpleDateFormat(format);
		else
			dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	}
	
	public static Object toObject(Class<?> clazz, String value) {
		if (Boolean.class == clazz)
			return Boolean.valueOf(value);
		if (Byte.class == clazz)
			return Byte.valueOf(value);		
		if (Date.class == clazz)
			return parseDate(value);
		if (Short.class == clazz)
			return Short.valueOf(value);
		if (Integer.class == clazz)
			return Integer.valueOf(value);
		if (Long.class == clazz)
			return Long.valueOf(value);
		if (Float.class == clazz)
			return Float.valueOf(value);
		if (Double.class == clazz)
			return Double.valueOf(value);
		return value;
	}
	
	public static boolean isPrimitive(Class<?> type)
	{
		return (type == int.class || type == long.class || type == double.class || type == float.class
				|| type == boolean.class || type == byte.class || type == char.class || type == short.class);
	}
	 
	 public static Class<?> boxPrimitiveClass(Class<?> type)
	 {
		if (type == int.class) {
			return Integer.class;
		} else if (type == long.class) {
			return Long.class;
		} else if (type == double.class) {
			return Double.class;
		} else if (type == float.class) {
			return Float.class;
		} else if (type == boolean.class) {
			return Boolean.class;
		} else if (type == byte.class) {
			return Byte.class;
		} else if (type == char.class) {
			return Character.class;
		} else if (type == short.class) {
			return Short.class;
		} else {
			String string = "Class '" + type.getName() + "' is not a primitive";
			throw new IllegalArgumentException(string);
		}
	 }
	
	public static int setSqlTypeConstants(Class<?> clazz)
	{
		if (Boolean.class == clazz)
			return java.sql.Types.BOOLEAN;
		if (Byte.class == clazz)
			return java.sql.Types.SMALLINT;		
		if (Date.class == clazz)
			return java.sql.Types.DATE;
		if (Short.class == clazz)
			return java.sql.Types.TINYINT;
		if (Integer.class == clazz)
			return java.sql.Types.INTEGER;
		if (Long.class == clazz)
			return java.sql.Types.BIGINT;
		if (Float.class == clazz)
			return java.sql.Types.FLOAT;
		if (Double.class == clazz)
			return java.sql.Types.DOUBLE;
		if (String.class == clazz)
			return java.sql.Types.VARCHAR;
		else
			return java.sql.Types.OTHER;
	}
	
	private static java.sql.Date parseDate(String value)
	{
		Date date = null;
		
		try
		{
			date = Date.valueOf(value);
		}
		catch(Exception e)
		{
			if (dateFormat != null)
			{
				try
				{
					date = convertUtilToSql(dateFormat.parse(value));
				}
				catch(Exception ex)
				{
					date = null;
				}
			}
		}
		
		return date;
	}
	
	private static java.sql.Date convertUtilToSql(java.util.Date uDate) 
	{
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }
	
	public static Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        return Long.valueOf(stringToConvert);
    }
	
	public static Integer convertToInteger(Object o){
        String stringToConvert = String.valueOf(o);
        return Integer.valueOf(stringToConvert);        
    }
	
	public static Double convertToDouble(Object o)
	{
		String stringToConvert = String.valueOf(o);
        return Double.valueOf(stringToConvert);
	}
	
	public static String convertToString(Object o){
        return String.valueOf(o);        
    }
	
	public static Date convertToDate(Object o){
        String stringToConvert = String.valueOf(o);
		return parseDate(stringToConvert);
    }
}
