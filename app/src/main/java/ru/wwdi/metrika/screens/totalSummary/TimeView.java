package ru.wwdi.metrika.screens.totalSummary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.TimeFormatter;
import ru.wwdi.metrika.models.TrafficSummaryStat;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class TimeView extends FrameLayout {

    private TextView mAverageTime;
    private TextView mMax;

    public TimeView(Context context) {
        super(context);
        init();
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_visit_time, this);
        mAverageTime = (TextView) findViewById(R.id.avarageTime);
        mMax = (TextView) findViewById(R.id.maxForDay);
    }

    public void setData(TrafficSummaryStatResponse data){
        mAverageTime.setText(TimeFormatter.formatTimeWithSeconds(data.getTotals().getVisitTime()));
        TrafficSummaryStat maxDay=null;
        for(TrafficSummaryStat stat: data.getTrafficSummaries()){
            if(stat.getVisitTime().equals(data.getMax().getVisitTime())){
                maxDay=stat;
            }
        }
        if(maxDay==null){
            mMax.setVisibility(GONE);
        }else{
            mMax.setVisibility(VISIBLE);
            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
            String date = format.format(maxDay.getDate());
            mMax.setText(getResources().getString(R.string.total_summary_time2, date, TimeFormatter.formatTimeWithSeconds(maxDay.getVisitTime())));
        }
    }
}
