package ru.wwdi.metrika.screens.base;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;

/**
 * Created by ryashentsev on 16.05.14.
 */
public class ChartLine extends FrameLayout {

    private TextView mTitle;
    private TextView mPerc;
    private View mBar;

    public ChartLine(Context context) {
        super(context);
        init();
    }

    public ChartLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.source_chart_line, this);
        mTitle = (TextView) findViewById(R.id.chartLine_title);
        mPerc = (TextView) findViewById(R.id.chartLine_perc);
        mBar = findViewById(R.id.chartLine_bar);
        mBar.setBackgroundColor(SharedPreferencesHelper.getSelectedCounter().getColor());
    }

    public void setData(Chart.ChartData data){
        mTitle.setText(Html.fromHtml(data.getName()));
        mPerc.setText(String.format("%.2f%%",data.getPerc()));
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mBar.getLayoutParams();
        lp.weight = (int)data.getPerc();
        mBar.setLayoutParams(lp);
    }
}
