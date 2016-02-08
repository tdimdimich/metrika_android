package ru.wwdi.metrika.screens.totalSummary;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.models.GenderStat;
import ru.wwdi.metrika.webservice.responses.AgeGenderStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class GenderView extends FrameLayout {

    private ImageView mMaleImage;
    private ImageView mFemaleImage;
    private TextView mMalePercValue;
    private TextView mFemalePercValue;
    private TextView mMalePercSign;
    private TextView mFemalePercSign;

    public GenderView(Context context) {
        super(context);
        init();
    }

    public GenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GenderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_gender, this);
        mMaleImage = (ImageView) findViewById(R.id.maleImage);
        mFemaleImage = (ImageView) findViewById(R.id.femaleImage);
        mMalePercValue = (TextView) findViewById(R.id.malePercentValue);
        mFemalePercValue = (TextView) findViewById(R.id.femalePercentValue);
        mMalePercSign = (TextView) findViewById(R.id.malePercentSign);
        mFemalePercSign = (TextView) findViewById(R.id.femalePercentSign);
    }

    public void setData(Counter counter, AgeGenderStatResponse data){
        List<GenderStat> list = data.getGenderStats();
        GenderStat femaleStat = null;
        GenderStat maleStat = null;
        if(list.get(0).getName().toLowerCase().equals("male") || list.get(0).getName().toLowerCase().equals("мужской")){
            maleStat = list.get(0);
            femaleStat = list.get(1);
        }else if(list.get(1).getName().toLowerCase().equals("male") || list.get(1).getName().toLowerCase().equals("мужской")){
            maleStat = list.get(1);
            femaleStat = list.get(0);
        }else{
            //не уверен что это нужно. Статистика яндекса вроде есть только в русском и английском языках.
            maleStat = list.get(0);
            femaleStat = list.get(1);
        }

        mMalePercValue.setText(String.format("%.1f", maleStat.getVisitsPercent()*100));
        mFemalePercValue.setText(String.format("%.1f", femaleStat.getVisitsPercent()*100));

        int highlightColor = counter.getColor();
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
        if(maleStat.getVisitsPercent()>femaleStat.getVisitsPercent()){
            mMalePercValue.setTextColor(counter.getColor());
            mMalePercSign.setTextColor(counter.getColor());
            mMaleImage.setColorFilter(colorFilter);

            int gray = Color.parseColor("#cecece");
            mFemalePercValue.setTextColor(gray);
            mFemalePercSign.setTextColor(gray);
            mFemaleImage.setColorFilter(gray);
        }else{
            mFemalePercValue.setTextColor(counter.getColor());
            mFemalePercSign.setTextColor(counter.getColor());
            mFemaleImage.setColorFilter(colorFilter);

            int gray = Color.parseColor("#cecece");
            mMalePercValue.setTextColor(gray);
            mMalePercSign.setTextColor(gray);
            mMaleImage.setColorFilter(gray);
        }
    }

}
