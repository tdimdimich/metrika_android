package ru.wwdi.metrika.screens.totalSummary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class VisitorsView extends FrameLayout {

    private TextView mCount;
    private TextView mMaxCountForDay;
    private View mBG;

    public VisitorsView(Context context) {
        super(context);
        init();
    }

    public VisitorsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisitorsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_visitors, this);
        mCount = (TextView) findViewById(R.id.totalVisitors);
        mMaxCountForDay = (TextView) findViewById(R.id.maxForDay);
        mBG = findViewById(R.id.bg);
    }

    public void setData(Counter counter, TrafficSummaryStatResponse data){
        mBG.setBackgroundColor(counter.getColor());
        mCount.setText(String.valueOf(data.getTotals().getVisitors()));
        mMaxCountForDay.setText(getResources().getString(R.string.total_summary_visitors2, data.getMax().getVisitors()));
    }

}
