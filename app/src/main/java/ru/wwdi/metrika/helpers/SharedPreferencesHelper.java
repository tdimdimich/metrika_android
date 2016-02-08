package ru.wwdi.metrika.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.activeandroid.query.Select;

import java.util.Date;

import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.models.DateInterval;

/**
 * Created by ryashentsev on 25.04.14.
 */
public class SharedPreferencesHelper {

    private static final String TOKEN_KEY = "token";
    private static final String SELECTED_COUNTER_ID_KEY = "selectedCounterId";
    private static final String START_DATE_KEY = "startDate";
    private static final String END_DATE_KEY = "endDate";
    private static final String FIRST_LAUNCH_KEY = "firstLaunch";
    private static final String LANGUAGE_KEY = "language";
    private static final String LAUNCH_COUNT_KEY = "lauchCount";
    private static final String DONT_SHOW_RATE_DIALOG_KEY = "dontShowRateDialog";

    private static final String ROTATE_TIP_SHOWN_KEY = "rotateTip";
    private static final String SWIPE_TIP_SHOWN_KEY = "swipeTip";

    private static Boolean sDontShowRateDialog;
    private static Integer sLaunchCount;
    private static String sLanguage;
    private static String sToken;
    private static DateInterval sCurrentDateInterval;
//    private static Counter sSelectedCounter;

    private static SharedPreferences sPreferences = YandexMetrikaApplication.getInstance().getSharedPreferences(YandexMetrikaApplication.class.getSimpleName(), Context.MODE_PRIVATE);
    private static SharedPreferences.Editor sEditor = sPreferences.edit();


    public static boolean isRotateTipShown(){
        return sPreferences.getBoolean(ROTATE_TIP_SHOWN_KEY, false);
    }
    public static boolean isSwipeTipShown(){
        return sPreferences.getBoolean(SWIPE_TIP_SHOWN_KEY, false);
    }
    public static void setRotateTipShown(){
        sEditor.putBoolean(ROTATE_TIP_SHOWN_KEY, true);
        sEditor.commit();
    }
    public static void setSwipeTipShown(){
        sEditor.putBoolean(SWIPE_TIP_SHOWN_KEY, true);
        sEditor.commit();
    }

    public static boolean isFirstLaunch(){
        return sPreferences.getBoolean(FIRST_LAUNCH_KEY, true);
    }

    public static void setNotFirstLaunch(){
        sEditor.putBoolean(FIRST_LAUNCH_KEY, false);
        sEditor.commit();
    }

/*
                        CURRENT DATE INTERVAL
 */

    public static DateInterval getCurrentDateInterval(){
        if(sCurrentDateInterval!=null) return sCurrentDateInterval;
        SharedPreferences preferences = sPreferences;
        if(preferences.contains(START_DATE_KEY) && preferences.contains(END_DATE_KEY)){
            Date startDate = new Date(preferences.getLong(START_DATE_KEY, System.currentTimeMillis()));
            Date endDate = new Date(preferences.getLong(END_DATE_KEY, System.currentTimeMillis()));
            return sCurrentDateInterval = new DateInterval(startDate, endDate);
        }else{
            return sCurrentDateInterval = DateHelper.getDefaultDateInterval();
        }
    }
    public static void setCurrentDateInterval(DateInterval dateInterval){
        if(dateInterval==null || dateInterval.getStartDate()==null || dateInterval.getEndDate()==null){
            sCurrentDateInterval = null;
            sEditor.remove(START_DATE_KEY);
            sEditor.remove(END_DATE_KEY);
        }else{
            sCurrentDateInterval = dateInterval;
            long millis = dateInterval.getStartDate().getTime();
            sEditor.putLong(START_DATE_KEY, millis);
            millis = dateInterval.getEndDate().getTime();
            sEditor.putLong(END_DATE_KEY, millis);
        }
        sEditor.commit();
    }

/*
                        CURRENT COUNTER
 */
    public static void setSelectedCounter(Counter counter){
//        sSelectedCounter = counter;
        sEditor.putLong(SELECTED_COUNTER_ID_KEY, counter.getCounterId());
        sEditor.commit();
    }
    public static Counter getSelectedCounter(){
//        if(sSelectedCounter==null){
        long id = sPreferences.getLong(SELECTED_COUNTER_ID_KEY, -1);
        if(id==-1) return null;
            /*sSelectedCounter = */
        return new Select().from(Counter.class).where("counter_id = ?",id).executeSingle();
//        }
//        return sSelectedCounter;
    }

/*
                        TOKEN
 */
public static void setToken(String token){
    sToken = token;
    sEditor.putString(TOKEN_KEY, token);
    sEditor.commit();
}
    public static String getToken(){
//        return "05dd3dd84ff948fdae2bc4fb91f13e22";
        if(sToken!=null) return sToken;
        return sToken = sPreferences.getString(TOKEN_KEY, null);
    }



    /*
                                LANGUAGE
     */
    public static void setLanguage(String language){
        sLanguage = language;
        sEditor.putString(LANGUAGE_KEY, language);
        sEditor.commit();
    }
    public static String getLanguage(){
        if(sLanguage!=null) return sLanguage;
        return sLanguage = sPreferences.getString(LANGUAGE_KEY, null);
    }



    /*
                                LAUNCH COUNT
     */
    public static void setDontShowRateDialog(){
        sDontShowRateDialog = true;
        sEditor.putBoolean(DONT_SHOW_RATE_DIALOG_KEY, true);
        sEditor.commit();
    }
    public static boolean getDontShowRateDialog(){
        if(sDontShowRateDialog!=null) return sDontShowRateDialog;
        return sDontShowRateDialog = sPreferences.getBoolean(DONT_SHOW_RATE_DIALOG_KEY, false);
    }

    public static void setLaunchCount(int count){
        sLaunchCount = count;
        sEditor.putInt(LAUNCH_COUNT_KEY, count);
        sEditor.commit();
    }
    public static int getLaunchCount(){
        if(sLaunchCount!=null) return sLaunchCount;
        return sLaunchCount = sPreferences.getInt(LAUNCH_COUNT_KEY, 0);
    }

}
