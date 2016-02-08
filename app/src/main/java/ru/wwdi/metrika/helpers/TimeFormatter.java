package ru.wwdi.metrika.helpers;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class TimeFormatter {

    public static String formatTimeWithSeconds(int sourceSeconds){
        int seconds = sourceSeconds;
        int hours = (seconds - seconds%3600)/3600;
        seconds -= hours*3600;
        int minutes = (seconds - seconds%60)/60;
        if(hours>0){
            return YandexMetrikaApplication.getInstance().getString(R.string.hours, 1f*sourceSeconds/3600);
        }else if(minutes>0){
            return YandexMetrikaApplication.getInstance().getString(R.string.minutes, 1f*sourceSeconds/60);
        }else{
            return YandexMetrikaApplication.getInstance().getString(R.string.seconds, sourceSeconds);
        }
    }

}
