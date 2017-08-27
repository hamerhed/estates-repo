package pl.hamerhed.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	public static boolean isToday(Date date) {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = df.format(date);
		String todayStr = df.format(today);
		return todayStr.equals(dateStr);
	}
}
