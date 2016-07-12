package customer.affeliateconsumer;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by roman rasskazov on 30.05.2015.
 */
public class DateParser {

    private static final Logger log = Logger.getLogger(DateParser.class);

    private static final String GMT = "GMT";
    private static final String CJ_TIME_DELIMETER = "T";

    private static final String DATE_FORMAT_CJ_COMMISSION = "yyyy-MM-ddHH:mm:ssZ";
    private static final String DATE_FORMAT_LINK_SHARE_EVENT = "E MMM dd yyyy HH:mm:ss Z";
    private static final String DATE_FORMAT_LINKSHARE = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMAT_LINKSHARE_ADVANCED = "yyyyMMdd";
    private static final String DATE_FORMAT_LINK_SHARE_SIGNATURE = "M/d/yyyyH:mm";
    private static final String DATE_FORMAT_CJ_COMMISSIONS = "yyyy-MM-dd";
    private static final String DATE_FORMAT_GMT = "yyyy-MM-dd HH:mm:ss.SSS";

    private static Date parseDate(String date, String format, String timezone){
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.UK);
        if (timezone != null) {
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            ExceptionLogger.logAndSendError(log, e, "parseLinkShareEventDate: can't parse date - " + date);
            return null;
        }
    }

    private static Date parseDate(String date, String format){
        return parseDate(date, format, null);
    }

    public static Date parseLinkShareEventDate(String date) {
        int gmtIndex = date.indexOf(GMT);
        if (gmtIndex>=0){
            date = date.substring(0, gmtIndex) + date.substring(gmtIndex + GMT.length());
        }
        return parseDate(date, DATE_FORMAT_LINK_SHARE_EVENT);
    }

    public static Date parseLinkShareSignatureDate(String date, String time) {
        return parseDate(date + time, DATE_FORMAT_LINK_SHARE_SIGNATURE, GMT);
    }

    public static Date parseCJDate(String date){
        int index = date.indexOf(CJ_TIME_DELIMETER);
        date = date.substring(0, index) + date.substring(index+1);
        return parseDate(date, DATE_FORMAT_CJ_COMMISSION);
    }

    private static String getTimeGMT(String dateFormat, int dayShift){
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(dateFormat);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone(GMT));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(GMT));
        calendar.add(Calendar.DATE, dayShift);
        return dateFormatGmt.format(calendar.getTime());
    }

    public static String getLinkShareTimeGMT(){
        return getTimeGMT(DATE_FORMAT_LINKSHARE, 0);
    }

    public static String getLinkShareAdvancedTimeGMT(){
        return getTimeGMT(DATE_FORMAT_LINKSHARE_ADVANCED, 0);
    }

    public static String getCJCommissionsTimeTomorrowGMT(){
        return getTimeGMT(DATE_FORMAT_CJ_COMMISSIONS, 1);
    }

    public static String getCJCommissionsTimeGMT(){
        return getTimeGMT(DATE_FORMAT_CJ_COMMISSIONS, 0);
    }

    public static String getTimeStampGMT(){
        return getTimeGMT(DATE_FORMAT_GMT, 0);
    }

}
