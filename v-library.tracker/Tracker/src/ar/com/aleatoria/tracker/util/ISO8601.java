package ar.com.aleatoria.tracker.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Helper class for handling ISO 8601 strings of the following format:
 * "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
 */
public final class ISO8601 {
    /** Transform Calendar to ISO 8601 string. */
    public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return fmt.format(date);
    }

    /** Get current date and time formatted as ISO 8601 string. */
    public static String now() {
        return fromCalendar(GregorianCalendar.getInstance());
    }

    /** Transform ISO 8601 string to Calendar. */
    public static Calendar toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calendar.setTime(fmt.parse(iso8601string)); 
        return calendar;
    }

	/**
	 * @param label A String date like 2009-03-12
	 * @return A tag of that dat Lun,Mar,Mie,etc.
	 * @throws ParseException
	 */
	public static String formatTinyTag(String label) throws ParseException {
		Calendar calendar = Calendar.getInstance();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        calendar.setTime(fmt.parse(label)); 
		//This is veri ugly, but if you have a better idea...
        String tag;
        switch(calendar.get(Calendar.DAY_OF_WEEK)) {
        	case Calendar.MONDAY:
        		tag = "Lun";
        	break;
        	case Calendar.TUESDAY:
        		tag = "Mar";
        	break;
        	case Calendar.WEDNESDAY:
        		tag = "Mie";
        	break;
        	case Calendar.THURSDAY:
        		tag = "Jue";
        	break;
        	case Calendar.FRIDAY:
        		tag = "Vie";
        	break;
        	case Calendar.SATURDAY:
        		tag = "Sab";
        	break;
        	case Calendar.SUNDAY:
        		tag = "Dom";
        	break;
        	default:
        		tag = "???";
        	break;
        }
        return tag;
	}

	/**
	 * @param weekendDay
	 * @return A String date in the format yyyy-MM-dd
	 */
	public static String formatTinyDate(Date weekendDay) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        return fmt.format(weekendDay); 
	}
}