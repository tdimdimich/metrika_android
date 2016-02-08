package ru.wwdi.metrika.screens.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by ryashentsev on 14.05.14.
 */
public class Chart extends LinearLayout {

    public static class ChartData{
        private String mName;
        private float mPerc;
        public ChartData(String name, float perc){
            mName = name;
            mPerc = perc;
        }
        public String getName() {
            return mName;
        }
        public float getPerc() {
            return mPerc;
        }
    }

    public Chart(Context context) {
        super(context);
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setData(List<ChartData> data){
        removeAllViews();
        ChartLine line;
        for(int i=0;i<data.size();i++){
            line = new ChartLine(getContext());
            line.setData(data.get(i));
            addView(line, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

}
