package ru.wwdi.metrika.screens.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.wwdi.metrika.R;

/**
 * Created by ryashentsev on 15.05.14.
 */
public class SourceSubline extends FrameLayout {

    public static class SublineData{
        private String mTitle;
        private String mValue;
        private float mPerc;
        public SublineData(String title, String value, float perc){
            mTitle = title;
            mValue = value;
            mPerc = perc;
        }
        public String getTitle() {
            return mTitle;
        }
        public String getValue() {
            return mValue;
        }
        public float getPerc() {
            return mPerc;
        }
    }

    private TextView mTitle;
    private TextView mValue;
    private TextView mPerc1;
    private TextView mPerc2;
    private View mBar;

    public SourceSubline(Context context) {
        super(context);
        init();
    }

    public SourceSubline(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceSubline(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.source_site_subline, this);
        mTitle = (TextView) findViewById(R.id.subLineTitle);
        mValue = (TextView) findViewById(R.id.subLineValue);
        mPerc1 = (TextView) findViewById(R.id.subLinePerc1);
        mPerc2 = (TextView) findViewById(R.id.subLinePerc2);
        mBar = findViewById(R.id.subLineBar);
    }

    public void setData(int color, SublineData data){
        mTitle.setText(data.getTitle());
        mValue.setText(data.getValue());
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBar.getLayoutParams();
        lp.weight = (int)data.getPerc();
        mBar.setLayoutParams(lp);
        mPerc1.setText(String.format("%.2f%%", data.getPerc()));
        mPerc2.setText(String.format("%.2f%%", data.getPerc()));
        if(data.getPerc()>50){
            mPerc1.setVisibility(GONE);
            mPerc2.setVisibility(VISIBLE);
        }else{
            mPerc1.setVisibility(VISIBLE);
            mPerc2.setVisibility(GONE);
        }

        mBar.setBackgroundColor(color);
        mValue.setTextColor(color);
    }
}
