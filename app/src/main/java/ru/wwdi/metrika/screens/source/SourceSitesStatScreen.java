package ru.wwdi.metrika.screens.source;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.TimeFormatter;
import ru.wwdi.metrika.models.SourceSitesStat;
import ru.wwdi.metrika.screens.base.BaseStatisticsScreen;
import ru.wwdi.metrika.screens.base.Chart;
import ru.wwdi.metrika.screens.base.SourceLine;
import ru.wwdi.metrika.screens.base.SourceSubline;
import ru.wwdi.metrika.views.ScrollViewWithDisablableScrolling;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.requests.SourceSitesStatRequest;
import ru.wwdi.metrika.webservice.responses.SourceSitesStatResponse;

/**
 * Created by ryashentsev on 16.05.14.
 */
public class SourceSitesStatScreen extends BaseStatisticsScreen {

    protected static enum DataType {
        VISITS,
        VIEWS,
        DENIAL,
        VIEW_DEPTH,
        VISIT_TIME
    }

    private SourceSitesStatRequest mRequest;
    private SourceSitesStatResponse mData;
    private DataType mChartDataType = DataType.VISITS;

    protected boolean isLoaded(){
        return mData!=null;
    }

    protected int getLinesCount(){
        return mData.getSourceSiteStats().size();
    }

    protected List<Chart.ChartData> getChartData(){
        List<Chart.ChartData> result = new ArrayList<Chart.ChartData>();
        Chart.ChartData data=null;
        for(int i=0;i<mData.getSourceSiteStats().size();i++){
            String url = mData.getSourceSiteStats().get(i).getUrl();
            if(mChartDataType==DataType.VIEWS){
                data = new Chart.ChartData(url, 100f*mData.getSourceSiteStats().get(i).getPageViews()/mData.getTotals().getPageViews());
            }else if(mChartDataType==DataType.VISITS){
                data = new Chart.ChartData(url, 100f*mData.getSourceSiteStats().get(i).getVisits()/mData.getTotals().getVisits());
            }else if(mChartDataType==DataType.DENIAL){
                data = new Chart.ChartData(url, 100f*mData.getSourceSiteStats().get(i).getDenial()/mData.getMax().getDenial());
            }else if(mChartDataType==DataType.VIEW_DEPTH){
                data = new Chart.ChartData(url, 100f*mData.getSourceSiteStats().get(i).getDepth()/mData.getMax().getDepth());
            }else if(mChartDataType==DataType.VISIT_TIME){
                data = new Chart.ChartData(url, 100f*mData.getSourceSiteStats().get(i).getVisitTime()/mData.getMax().getVisitTime());
            }
            result.add(data);
        }
        return result;
    }

    protected SourceLine.LineData getLineData(int position){
        final SourceSitesStat data = mData.getSourceSiteStats().get(position);
        final SourceSitesStat total = mData.getTotals();
        final SourceSitesStat max = mData.getMax();
        String title = data.getUrl()+":";
        String value = String.valueOf(data.getVisits());
        SourceSubline.SublineData sublineData = null;
        List<SourceSubline.SublineData> sublineDatas = new ArrayList<SourceSubline.SublineData>();
        for(int i=0;i<DataType.values().length;i++){
            if(DataType.values()[i]==DataType.VISITS){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_visits), String.valueOf(data.getVisits()), 100f*data.getVisits()/total.getVisits());
            }else if(DataType.values()[i]==DataType.VIEWS){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_views), String.valueOf(data.getPageViews()), 100f*data.getPageViews()/total.getPageViews());
            }else if(DataType.values()[i]==DataType.DENIAL){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_denial), String.format("%d%%", (int) (100 * data.getDenial())), 100f*data.getDenial());
            }else if(DataType.values()[i]==DataType.VIEW_DEPTH){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_depth), String.format("%.2f",data.getDepth()), 100f*data.getDepth()/max.getDepth());
            }else if(DataType.values()[i]==DataType.VISIT_TIME){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_time), TimeFormatter.formatTimeWithSeconds(data.getVisitTime()), 100f*data.getVisitTime()/max.getVisitTime());
            }
            sublineDatas.add(sublineData);
        }
        return new SourceLine.LineData(title, value, sublineDatas);
    }

    protected String[] getSpinnerStringsArray(){
        return new String[]{
                getResources().getString(R.string.line_visits),
                getResources().getString(R.string.line_views),
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

        mRequest = Webservice.getSourceSitesStat(!needRefresh(), new BaseRequest.Listener<SourceSitesStatResponse>() {
            @Override
            public void onResponse(SourceSitesStatResponse response) {
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


    protected void addLine(ViewGroup container, int position){

    }
}
