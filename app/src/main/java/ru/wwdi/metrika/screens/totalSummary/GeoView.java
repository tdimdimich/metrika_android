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
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.models.GeoStat;
import ru.wwdi.metrika.webservice.responses.GeoStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class GeoView extends FrameLayout {

    private ImageView mImage2;
    private TextView mFirstCountryName;
    private TextView mFirstCountryValue;
    private TextView mSecondCountryName;
    private TextView mSecondCountryValue;
    private TextView mThirdCountryName;
    private TextView mThirdCountryValue;

    public GeoView(Context context) {
        super(context);
        init();
    }

    public GeoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GeoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_geo, this);
        mImage2 = (ImageView) findViewById(R.id.image2);
        mFirstCountryName = (TextView) findViewById(R.id.firstCountryName);
        mFirstCountryValue = (TextView) findViewById(R.id.firstCountryValue);
        mSecondCountryName = (TextView) findViewById(R.id.secondCountryName);
        mSecondCountryValue = (TextView) findViewById(R.id.secondCountryValue);
        mThirdCountryName = (TextView) findViewById(R.id.thirdCountryName);
        mThirdCountryValue = (TextView) findViewById(R.id.thirdCountryValue);
    }

    public void setData(Counter counter, GeoStatResponse data){
        int highlightColor = counter.getColor();
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
        mImage2.setColorFilter(colorFilter);

        List<GeoStat> list = data.getGeoStats();
        if(list.size()>0){
            mFirstCountryName.setVisibility(VISIBLE);
            mFirstCountryValue.setVisibility(VISIBLE);
            mFirstCountryName.setText(list.get(0).getName());
            mFirstCountryValue.setText(getResources().getQuantityString(R.plurals.visits, list.get(0).getVisits(), list.get(0).getVisits()));
            mFirstCountryName.setTextColor(counter.getColor());
            mFirstCountryValue.setTextColor(counter.getColor());
        }else{
            mFirstCountryName.setVisibility(INVISIBLE);
            mFirstCountryValue.setVisibility(INVISIBLE);
        }
        if(list.size()>1){
            mSecondCountryName.setVisibility(VISIBLE);
            mSecondCountryValue.setVisibility(VISIBLE);
            mSecondCountryName.setText(list.get(1).getName());
            mSecondCountryValue.setText(getResources().getQuantityString(R.plurals.visits, list.get(1).getVisits(), list.get(1).getVisits()));
        }else{
            mSecondCountryName.setVisibility(INVISIBLE);
            mSecondCountryValue.setVisibility(INVISIBLE);
        }
        if(list.size()>2){
            mThirdCountryName.setVisibility(VISIBLE);
            mThirdCountryValue.setVisibility(VISIBLE);
            mThirdCountryName.setText(list.get(2).getName());
            mThirdCountryValue.setText(getResources().getQuantityString(R.plurals.visits, list.get(2).getVisits(), list.get(2).getVisits()));
        }else{
            mThirdCountryName.setVisibility(INVISIBLE);
            mThirdCountryValue.setVisibility(INVISIBLE);
        }

    }

}
