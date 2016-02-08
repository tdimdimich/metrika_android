package ru.wwdi.metrika.helpers;

import android.util.DisplayMetrics;

import ru.wwdi.metrika.YandexMetrikaApplication;

/**
 * Created by ryashentsev on 04.05.14.
 */
public class PxDpHelper {

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = YandexMetrikaApplication.getInstance().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = YandexMetrikaApplication.getInstance().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

}
