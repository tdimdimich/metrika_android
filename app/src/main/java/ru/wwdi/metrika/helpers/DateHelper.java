package ru.wwdi.metrika.helpers;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.models.DateInterval;

/**
 * Created by ryashentsev on 03.05.14.
 */
public class DateHelper {

    public static final int TODAY = 0;
    public static final int WEEK = 1;
    public static final int MONTH = 2;
    public static final int YEAR = 3;

    /**
     * Transforms string representation of date received from server to Date object
     * @param strDate
     * @return
     */
    public static Date getDateFromString(String strDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            return format.parse(strDate);
        } catch (ParseException e) {
            Log.e(YandexMetrikaApplication.class.getSimpleName(), "Can't convert string to date", e);
        }
        return new Date();
    }

    /**
     * Transforms date to string representation of date for sending to server
     * @param date
     * @return
     */
    public static String getStringFromDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(date);
    }



    public static Date getTodayDate(){
        Calendar calendar = today();
        return calendar.getTime();
    }

    public static Date getYesterdayDate(){
        Calendar calendar = today();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTime();
    }

    public static Date getDateWeekAgo(){
        Calendar calendar = today();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        return calendar.getTime();
    }

    public static Date getDateMonthAgo(){
        Calendar calendar = today();
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTime();
    }

    public static Date getDateYearAgo(){
        Calendar calendar = today();
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTime();
    }

    private static Calendar today(){
        Calendar calendar = new GregorianCalendar();
        calendar = removeHoursMinutesSecondsAndMilliseconds(calendar);
        return calendar;
    }

    private static Date removeHoursMinutesSecondsAndMilliseconds(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar = removeHoursMinutesSecondsAndMilliseconds(calendar);
        return calendar.getTime();
    }
    private static Calendar removeHoursMinutesSecondsAndMilliseconds(Calendar calendar){
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }


    public static String formatDate(Date date){
        return formatDate(date, true);
    }
    public static String formatDate(Date date, boolean withYear){
        Locale current = YandexMetrikaApplication.getInstance().getResources().getConfiguration().locale;
        SimpleDateFormat df;
        if(current.getLanguage().equals("ru")){
            if(withYear){
                df = new SimpleDateFormat("dd MMMM yyyy");
            }else{
                df = new SimpleDateFormat("dd MMMM");
            }
        }else{
            //15 мая 1948 года – 15th May, 1948
            if(withYear){
                df = new SimpleDateFormat("dd'th' MMMM, yyyy");
            }else{
                df = new SimpleDateFormat("dd'th' MMMM");
            }
        }
        return df.format(date);
    }

    public static DateInterval getDefaultDateInterval(){
        return getDateInterval(WEEK);
    }

    public static DateInterval getDateInterval(int interval){
        switch (interval){
            case TODAY:
                return new DateInterval(getTodayDate(), getTodayDate());
            case WEEK:
                return new DateInterval(getDateWeekAgo(), getYesterdayDate());
            case MONTH:
                return new DateInterval(getDateMonthAgo(), getYesterdayDate());
            case YEAR:
                return new DateInterval(getDateYearAgo(), getYesterdayDate());
        }
        return null;
    }

}
