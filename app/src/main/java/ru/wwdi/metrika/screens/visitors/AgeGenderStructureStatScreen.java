package ru.wwdi.metrika.screens.visitors;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.TimeFormatter;
import ru.wwdi.metrika.models.AgeGenderStat;
import ru.wwdi.metrika.screens.base.BaseStatisticsScreen;
import ru.wwdi.metrika.screens.base.Chart;
import ru.wwdi.metrika.screens.base.SourceLine;
import ru.wwdi.metrika.screens.base.SourceSubline;
import ru.wwdi.metrika.views.ScrollViewWithDisablableScrolling;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.AgeGenderStructureStatRequest;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.responses.AgeGenderStructureStatResponse;

/**
 * Created by ryashentsev on 16.05.14.
 */
public class AgeGenderStructureStatScreen extends BaseStatisticsScreen {

    protected static enum DataType {
        VISITS,
        VISITS_PERC,
        DENIAL,
        VIEW_DEPTH,
        VISIT_TIME
    }

    private AgeGenderStructureStatRequest mRequest;
    private AgeGenderStructureStatResponse mData;
    private DataType mChartDataType = DataType.VISITS;

    protected boolean isLoaded(){
        return mData!=null;
    }

    protected int getLinesCount(){
        if(mData==null) return 0;
        return mData.getStats().size();
    }

    protected List<Chart.ChartData> getChartData(){
        List<Chart.ChartData> result = new ArrayList<Chart.ChartData>();
        Chart.ChartData data=null;

        float maxDepth = 0;
        for(int i=0;i<mData.getStats().size();i++){
            maxDepth = Math.max(maxDepth, mData.getStats().get(i).getDepth());
        }

        for(int i=0;i<getLinesCount();i++){
            String name = mData.getStats().get(i).getNameGender()+", "+mData.getStats().get(i).getName();
            if(mChartDataType==DataType.VISITS_PERC){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getVisitsPercent());
            }else if(mChartDataType== DataType.VISITS){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getVisits()/mData.getTotals().getVisits());
            }else if(mChartDataType== DataType.DENIAL){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getDenial());
            }else if(mChartDataType== DataType.VIEW_DEPTH){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getDepth()/maxDepth);
            }else if(mChartDataType== DataType.VISIT_TIME){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getVisitTime()/mData.getMax().getVisitTime());
            }
            result.add(data);
        }
        return result;
    }

    protected SourceLine.LineData getLineData(int position){
        final AgeGenderStat data = mData.getStats().get(position);
        final AgeGenderStat total = mData.getTotals();
        final AgeGenderStat max = mData.getMax();

        float maxDepth = 0;
        for(int i=0;i<mData.getStats().size();i++){
            maxDepth = Math.max(maxDepth, mData.getStats().get(i).getDepth());
        }


        String title = data.getNameGender()+", "+data.getName()+":";
        String value = String.valueOf(data.getVisits());
        SourceSubline.SublineData sublineData = null;
        List<SourceSubline.SublineData> sublineDatas = new ArrayList<SourceSubline.SublineData>();
        for(int i=0;i< DataType.values().length;i++){
            if(DataType.values()[i]== DataType.VISITS){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_visits), String.valueOf(data.getVisits()), 100f*data.getVisits()/total.getVisits());
            }else if(DataType.values()[i]== DataType.VISITS_PERC){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_visits_perc), String.valueOf(data.getVisitsPercent()), 100f*data.getVisitsPercent());
            }else if(DataType.values()[i]== DataType.DENIAL){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_denial), String.format("%d%%", (int) (100 * data.getDenial())), 100f*data.getDenial());
            }else if(DataType.values()[i]== DataType.VIEW_DEPTH){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_depth), String.format("%.2f",data.getDepth()), 100f*data.getDepth()/maxDepth);
            }else if(DataType.values()[i]== DataType.VISIT_TIME){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_time), TimeFormatter.formatTimeWithSeconds(data.getVisitTime()), 100f*data.getVisitTime()/max.getVisitTime());
            }
            sublineDatas.add(sublineData);
        }
        return new SourceLine.LineData(title, value, sublineDatas);
    }

    protected String[] getSpinnerStringsArray(){
        return new String[]{
                getResources().getString(R.string.line_visits),
                getResources().getString(R.string.line_visits_perc),
                getResources().getString(R.string.line_denial),
                getResources().getString(R.string.line_depth),
                getResources().getString(R.string.line_time)
        };
    }

    protected void onSpinnerItemSelected(int position){
        mChartDataType = DataType.values()[position];
    }

    protected int getSpinnerSelectedPosition(){
        return mChartDataType.ordinal();
    }

    protected void load(){
        View v = getView();
        SwipeRefreshLayout srl = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        srl.setEnabled(false);
        ScrollViewWithDisablableScrolling scrollView = (ScrollViewWithDisablableScrolling) v.findViewById(R.id.scrollView);
        scrollView.setScrollingEnabled(false);

        mRequest = Webservice.getAgeGenderStructureStat(!needRefresh(), new BaseRequest.Listener<AgeGenderStructureStatResponse>() {
            @Override
            public void onResponse(AgeGenderStructureStatResponse response) {
                if (response.hasErrors()) {
                    if(response.hasNoDataError()){
                        showNoData();
                    }else{
                        showError();
                    }
                } else {
                    mData = response;
                    showContent();
                }
            }
        });
    }

    protected void reset(){
        if(mRequest!=null) mRequest.stop();
        mData = null;
    }

}
