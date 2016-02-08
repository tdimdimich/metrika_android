package ru.wwdi.metrika.screens.totalSummary;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.AgeStat;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.webservice.responses.AgeGenderStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class AgeView extends FrameLayout {

    private ImageView mImage2;
    private TextView mFirstAgeName;
    private TextView mFirstAgeValue;
    private TextView mSecondAgeName;
    private TextView mSecondAgeValue;
    private TextView mThirdAgeName;
    private TextView mThirdAgeValue;

    public AgeView(Context context) {
        super(context);
        init();
    }

    public AgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AgeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_age, this);
        mImage2 = (ImageView) findViewById(R.id.image2);
        mFirstAgeName = (TextView) findViewById(R.id.firstAgeName);
        mFirstAgeValue = (TextView) findViewById(R.id.firstAgeValue);
        mSecondAgeName = (TextView) findViewById(R.id.secondAgeName);
        mSecondAgeValue = (TextView) findViewById(R.id.secondAgeValue);
        mThirdAgeName = (TextView) findViewById(R.id.thirdAgeName);
        mThirdAgeValue = (TextView) findViewById(R.id.thirdAgeValue);
    }

    public void setData(Counter counter, AgeGenderStatResponse data){
        int highlightColor = counter.getColor();
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
        mImage2.setColorFilter(colorFilter);

        List<AgeStat> list = data.getAgeStats();
        if(list.size()>0){
            mFirstAgeName.setVisibility(VISIBLE);
            mFirstAgeValue.setVisibility(VISIBLE);
            mFirstAgeName.setText(list.get(0).getName());
            mFirstAgeValue.setText(String.format("(%.1f%%)", list.get(0).getVisitsPercent()*100));
            mFirstAgeName.setTextColor(counter.getColor());
            mFirstAgeValue.setTextColor(counter.getColor());
        }else{
            mFirstAgeName.setVisibility(GONE);
            mFirstAgeValue.setVisibility(GONE);
        }
        if(list.size()>1){
            mSecondAgeName.setVisibility(VISIBLE);
            mSecondAgeValue.setVisibility(VISIBLE);
            mSecondAgeName.setText(list.get(1).getName());
            mSecondAgeValue.setText(String.format("(%.1f%%)", list.get(1).getVisitsPercent()*100));
        }else{
            mSecondAgeName.setVisibility(GONE);
            mSecondAgeValue.setVisibility(GONE);
        }
        if(list.size()>2){
            mThirdAgeName.setVisibility(VISIBLE);
            mThirdAgeValue.setVisibility(VISIBLE);
            mThirdAgeName.setText(list.get(2).getName());
            mThirdAgeValue.setText(String.format("(%.1f%%)", list.get(2).getVisitsPercent()*100));
        }else{
            mThirdAgeName.setVisibility(GONE);
            mThirdAgeValue.setVisibility(GONE);
        }

    }

}
