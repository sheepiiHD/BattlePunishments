package com.lducks.battlepunishments.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeConverter {
	/**
	 * @param before Time to start at
	 * @param time ex: 1d,5s
	 * @return Time in long
	 * @throws Exception
	 */
	public static long convertToLong(long before, String time) throws Exception{
		Calendar cal = new GregorianCalendar();
		
		String[] groupings = time.split(",");
		if (groupings.length == 0)
			return 0L;

		cal.setTimeInMillis(before);
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
	public static String convertLongToDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat(BattleSettings.getDateFormat());
		sdf.setTimeZone(TimeZone.getTimeZone(BattleSettings.getTimeZone()));
		return sdf.format(time);
	}
}
