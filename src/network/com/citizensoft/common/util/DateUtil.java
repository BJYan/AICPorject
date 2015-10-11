package network.com.citizensoft.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static Date parseDate(String value) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
    }
    
    public static String parseDate(Date value) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
    }
    
    public static String getCurrentDate()
    {
    	return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }
}
