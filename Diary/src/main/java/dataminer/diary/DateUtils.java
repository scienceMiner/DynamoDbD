package dataminer.diary;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DateUtils {

	private static Logger logger =  LogManager.getLogger( DateUtils.class );

	public static Date formDate(EntryType e)
	{
		e.getDay();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, e.getDay().intValue());
		cal.set(Calendar.MONTH, mapMonth(e.getMonth()));
		cal.set(Calendar.YEAR, e.getYear().intValue());
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		return cal.getTime();
		
	}
	
	public static String formDayMonth(int day, int mon)
	{
		String t1 = mapMonth(mon) + " " + convert(day);
		
		logger.info("DATESEARCH:"+t1);
		
		return t1;
	}
	
	public static String convert(int day)
	{
		if (day < 10)
			return new String("0"+day);
		else
			return new Integer(day).toString();
	}
	
	public static Date formDate(Date date)
	{
		
		//e.getDay();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		//cal.set(Calendar.DAY_OF_MONTH, e.getDay().intValue());
		//cal.set(Calendar.MONTH, mapMonth(e.getMonth()));
		//cal.set(Calendar.YEAR, e.getYear().intValue());
		
		return cal.getTime();
	}
	
	public static String mapDigitTo2DigitString(BigInteger day) {
		if (day != null)
		{
			if (day.intValue() == 1)
				return "01";
			else if (day.intValue() == 2)
				return "02";
			else if (day.intValue() == 3)
				return "03";
			else if (day.intValue() == 4)
				return "04";
			else if (day.intValue() == 5)
				return "05";
			else if (day.intValue() == 6)
				return "06";
			else if (day.intValue() == 7)
				return "07";
			else if (day.intValue() == 8)
				return "08";
			else if (day.intValue() == 9)
				return "09";
			else if (day.intValue() > 9)
				return day.toString();
			
		}
		
		return "";
	}
	
	public static Instant createInstantDate(EntryType eType ) {
		
		 // Create an Instant value.
        eType.getDay();
        eType.getMonth();
        String twoDigitMonth = mapMonthTo2DigitString(eType.getMonth());
        BigInteger day = eType.getDay() ;
        
        String value = eType.getYear() + "-" + twoDigitMonth + "-" + mapDigitTo2DigitString(day);
        
        LocalDate localDate = LocalDate.parse(value);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);

        return instant;
        
	}
	
	public static String mapMonthTo2DigitString(String month)
	{
		if (month != null)
		{
			if (month.equalsIgnoreCase("Jan") || month.equalsIgnoreCase("January"))
				return "01";
			else if (month.equalsIgnoreCase("Feb") || month.equalsIgnoreCase("February"))
				return "02";
			else if (month.equalsIgnoreCase("Mar") || month.equalsIgnoreCase("March"))
				return "03";
			else if (month.equalsIgnoreCase("Apr") || month.equalsIgnoreCase("April"))
				return "04";
			else if (month.equalsIgnoreCase("May"))
				return "05";
			else if (month.equalsIgnoreCase("Jun") || month.equalsIgnoreCase("June"))
				return "06";
			else if (month.equalsIgnoreCase("Jul") || month.equalsIgnoreCase("July"))
				return "07";
			else if (month.equalsIgnoreCase("Aug") || month.equalsIgnoreCase("August"))
				return "08";
			else if (month.equalsIgnoreCase("Sep") || month.equalsIgnoreCase("September"))
				return "09";
			else if (month.equalsIgnoreCase("Oct") || month.equalsIgnoreCase("October"))
				return "10";
			else if (month.equalsIgnoreCase("Nov") || month.equalsIgnoreCase("November"))
				return "11";
			else if (month.equalsIgnoreCase("Dec") || month.equalsIgnoreCase("December"))
				return "12";
		}
		
		logger.info(" USE DEFAULT MONTH " );

		return "01";
		
	}
	
	public static String mapIntMonthTo2DigitString(Integer month)
	{
		if (month != null)
		{
			if (month==1)
				return "01";
			else if (month==2)
				return "02";
			else if (month==3)
				return "03";
			else if (month==4)
				return "04";
			else if (month==5)
				return "05";
			else if (month==6)
				return "06";
			else if (month==7)
				return "07";
			else if (month==8)
				return "08";
			else if (month==9)
				return "09";
			else if (month==10)
				return "10";
			else if (month==11)
				return "11";
			else if (month==12)
				return "12";
		}
		
		return null;
		
	}
	
	
	public static int mapMonth(String month)
	{
		if (month != null)
		{
			if (month.equalsIgnoreCase("Jan") || month.equalsIgnoreCase("January"))
				return 0;
			else if (month.equalsIgnoreCase("Feb") || month.equalsIgnoreCase("February"))
				return 1;
			else if (month.equalsIgnoreCase("Mar") || month.equalsIgnoreCase("March"))
				return 2;
			else if (month.equalsIgnoreCase("Apr") || month.equalsIgnoreCase("April"))
				return 3;
			else if (month.equalsIgnoreCase("May"))
				return 4;
			else if (month.equalsIgnoreCase("Jun") || month.equalsIgnoreCase("June"))
				return 5;
			else if (month.equalsIgnoreCase("Jul") || month.equalsIgnoreCase("July"))
				return 6;
			else if (month.equalsIgnoreCase("Aug") || month.equalsIgnoreCase("August"))
				return 7;
			else if (month.equalsIgnoreCase("Sep") || month.equalsIgnoreCase("September"))
				return 8;
			else if (month.equalsIgnoreCase("Oct") || month.equalsIgnoreCase("October"))
				return 9;
			else if (month.equalsIgnoreCase("Nov") || month.equalsIgnoreCase("November"))
				return 10;
			else if (month.equalsIgnoreCase("Dec") || month.equalsIgnoreCase("December"))
				return 11;
		}
		
		return 0;
		
	}
	
	
	public static String mapMonth(int month)
	{
		
			if (month == 0)
				return "Jan";
			else if (month == 1)
				return "Feb";
			else if (month == 2)
				return "Mar";
			else if (month == 3)
				return "Apr";
			else if (month == 4)
				return "May";
			else if (month == 5)
				return "Jun";
			else if (month == 6)
				return "Jul";
			else if (month == 7)
				return "Aug";
			else if (month == 8)
				return "Sep";
			else if (month == 9)
				return "Oct";
			else if (month == 10)
				return "Nov";
			else if (month == 11)
				return "Dec";
							
		return "Jan";
		
	}
	
}
