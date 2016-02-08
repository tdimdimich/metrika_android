package ru.wwdi.metrika.screens.traffic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.GregorianCalendar;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.models.DateInterval;

/**
 * Created by ryashentsev on 13.05.14.
 */
public class TrafficStatSubline extends FrameLayout {

    private TextView mPeriod;
    private TextView mValue;
    private TextView mPerc1;
    private TextView mPerc2;
    private View mBar;
    private View mBarContainer;

    public TrafficStatSubline(Context context) {
        super(context);
        init();
    }

    public TrafficStatSubline(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrafficStatSubline(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.traffic_stat_subline, this);
        mPerc1 = (TextView) findViewById(R.id.subLinePerc1);
        mPerc2 = (TextView) findViewById(R.id.subLinePerc2);
        mValue = (TextView) findViewById(R.id.subLineValue);
        mPeriod = (TextView) findViewById(R.id.subLinePeriod);
        mBar = findViewById(R.id.subLineBar);
        mBarContainer = findViewById(R.id.barContainer);
    }

    public void setData(DateInterval interval, String value, float perc){
        //period text
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(interval.getStartDate());
        String from = DateHelper.formatDate(interval.getStartDate(), false);
        calendar.setTime(interval.getEndDate());
        String to = DateHelper.formatDate(interval.getEndDate(), false);
        mPeriod.setText(from+" - "+to);

        //value and percent
        mPerc1.setText(String.format("%.2f%%", perc));
        mPerc2.setText(String.format("%.2f%%", perc));
        mValue.setText(value);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mBar.getLayoutParams();
        params.weight = (int)perc;
        mBar.setLayoutParams(params);

        if(perc>65){
            mPerc1.setVisibility(GONE);
            mPerc2.setVisibility(VISIBLE);
        }else{
            mPerc1.setVisibility(VISIBLE);
            mPerc2.setVisibility(GONE);
        }
    }

    public void setColor(int color){
        mValue.setTextColor(color);
        mBar.setBackgroundColor(color);
    }

}
