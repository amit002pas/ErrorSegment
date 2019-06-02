package com.yodlee.health.errorsegment.aop;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeZoneTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(TimeZone.getAvailableIDs());
		for(String str:TimeZone.getAvailableIDs())
			System.out.println(str);
		
		Calendar cal=Calendar.getInstance(TimeZone.getTimeZone("PST"));
		System.out.println("^^"+cal.getTime());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 'T' HH:mm'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DATE),cal.get(Calendar.HOUR),cal.get(Calendar.MINUTE));
		
		System.out.println("^^"+sdf.format(cal.getTime()));

	}

}
