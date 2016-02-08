package ru.wwdi.metrika.screens.totalSummary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.SourcesSummaryStat;
import ru.wwdi.metrika.webservice.responses.SourcesSummaryStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class SourcesView extends FrameLayout {

    private TextView mFirstTypeValue;
    private TextView mFirstTypeName;
    private TextView mSecondSource;
    private TextView mThirdSource;

    public SourcesView(Context context) {
        super(context);
        init();
    }

    public SourcesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourcesView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_sources, this);
        mFirstTypeValue = (TextView) findViewById(R.id.firstTypeValue);
        mFirstTypeName = (TextView) findViewById(R.id.firstTypeName);
        mSecondSource = (TextView) findViewById(R.id.secondSource);
        mThirdSource = (TextView) findViewById(R.id.thirdSource);
    }

    public void setData(SourcesSummaryStatResponse data){
        List<SourcesSummaryStat> list = data.getSourceSummaries();
        mFirstTypeName.setText(list.get(0).getName().toLowerCase()+ "");
        mFirstTypeValue.setText(String.valueOf(list.get(0).getVisits()));
        if(list.size()>1){
            mSecondSource.setVisibility(VISIBLE);
            mSecondSource.setText(getResources().getString(R.string.total_summary_source, list.get(1).getVisits(), list.get(1).getName()));
        }else{
            mSecondSource.setVisibility(GONE);
        }
        if(list.size()>2){
            mThirdSource.setVisibility(VISIBLE);
            mThirdSource.setText(getResources().getString(R.string.total_summary_source, list.get(2).getVisits(), list.get(2).getName()));
        }else{
            mThirdSource.setVisibility(GONE);
        }
    }

}
