package com.lducks.battlepunishments.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeConverter {
	/**
	 * Calendar type, default GregorianCalendar();
	 */
	static Calendar cal = new GregorianCalendar();

	/**
	 * 
	 * @param time ex: 1d,5s
	 * @return Time in long
	 * @throws Exception
	 */
	public static long convertToLong(String time) throws Exception{
		String[] groupings = time.split(",");
		if (groupings.length == 0)
			return 0L;

		cal.setTimeInMillis(System.currentTimeMillis());
		for (String str : groupings) {
			if(str.length() == 0) {
				throw new Exception("Unknown date value specified.");
			}

			int type;
			switch (str.charAt(str.length() - 1)) {
			case 'm':
				type = Calendar.MINUTE;
				break;
			case 'h':
				type = Calendar.HOUR;
				break;
			case 'd':
				type = Calendar.DATE;
				break;
			case 'w':
				type = Calendar.WEEK_OF_YEAR;
				break;
			case 'y':
				type = Calendar.YEAR;
				break;
			case 's':
				type = Calendar.SECOND;
				break;
			default:
				throw new Exception("Unknown date value specified.");
			}

			cal.add(type, Integer.valueOf(str.substring(0, str.length() -1)));
		}
		return cal.getTimeInMillis();
	}

	/**
	 * 
	 * @param time in long
	 * @return time in calendar date
	 */
	public static String convertToString(long time) {
		cal.setTimeInMillis(time);
		return cal.getTime().toString();
	}
}
