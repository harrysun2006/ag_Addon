package com.agloco.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public final static String DATE_PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
	private final static DateFormat DF_STANDARD = new SimpleDateFormat(DATE_PATTERN_STANDARD);
	
	public static String getDate2String(Date date){
		return getDate2String(date,DF_STANDARD);
	}
	
	private static String getDate2String(Date date,DateFormat df){
		if(date == null){
			return StringUtil.EMPTY_STRING;	
		}
		return df.format(date);
	}
}
