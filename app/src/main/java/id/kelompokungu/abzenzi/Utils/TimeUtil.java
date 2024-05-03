package id.kelompokungu.abzenzi.Utils;

import java.util.*;

public class TimeUtil
{
	public static String getDateFormat() {
		Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String format = year + "-" + month + "-" + day;
		return format;
	}
}
