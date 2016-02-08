package ru.wwdi.metrika.helpers;

import android.graphics.Color;

/**
 * Created by ryashentsev on 04.05.14.
 */
public class CounterColorsHelper {

    private static final Integer[] sGradientColors = {
            Color.rgb(199, 47, 47),
            Color.rgb(194, 46, 102),
            Color.rgb(188, 42, 94),
            Color.rgb(132, 40, 202),
            Color.rgb(67, 80, 183),
            Color.rgb(46, 133, 194),

            Color.rgb(37, 174, 191),
            Color.rgb(134, 197, 36),
            Color.rgb(195, 137, 34),
            Color.rgb(104, 97, 186),
            Color.rgb(94, 125, 160),
            Color.rgb(43, 143, 94),

            Color.rgb(41, 176, 22),
            Color.rgb(114, 78, 42),
            Color.rgb(158, 171, 73),
            Color.rgb(167, 55, 66),
    };

    private static final Integer[] sColors = {
            Color.rgb(232, 38, 38),
            Color.rgb(232, 38, 111),
            Color.rgb(225, 38, 232),
            Color.rgb(148, 38, 232),
            Color.rgb(60, 71, 217),
            Color.rgb(38, 152, 232),

            Color.rgb(38, 211, 232),
            Color.rgb(155, 233, 33),
            Color.rgb(232, 161, 38),
            Color.rgb(128, 118, 238),
            Color.rgb(127, 163, 203),
            Color.rgb(56, 187, 123),

            Color.rgb(52, 232, 27),
            Color.rgb(155, 109, 64),
            Color.rgb(199, 214, 97),
            Color.rgb(214, 76, 89),
    };

    public static Integer[] getColors(){
        return sColors;
    }

    public static int getGradientColorForcolor(int color){
        for(int i=0;i<sColors.length;i++){
            if(sColors[i]==color) return sGradientColors[i];
        }
        return sGradientColors[0];
    }

    public static int[] getRandomColors(){
        int position = (int) Math.random()*sColors.length;
        return new int[]{sColors[position], sGradientColors[position]};
    }

}
