package ru.wwdi.metrika.screens.content;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.PageTitleStat;
import ru.wwdi.metrika.screens.base.BaseStatisticsScreen;
import ru.wwdi.metrika.screens.base.Chart;
import ru.wwdi.metrika.screens.base.SourceLine;
import ru.wwdi.metrika.screens.base.SourceSubline;
import ru.wwdi.metrika.views.ScrollViewWithDisablableScrolling;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.requests.PageTitleStatRequest;
import ru.wwdi.metrika.webservice.responses.PageTitleStatResponse;

/**
 * Created by ryashentsev on 16.05.14.
 */
public class PageTitleStatScreen extends BaseStatisticsScreen {

    protected static enum DataType {
        VIEWS
    }

    private PageTitleStatRequest mRequest;
    private PageTitleStatResponse mData;
    private DataType mChartDataType = DataType.VIEWS;

    protected boolean isLoaded(){
        return mData!=null;
    }

    protected int getLinesCount(){
        return mData.getStats().size();
    }

    protected List<Chart.ChartData> getChartData(){
        List<Chart.ChartData> result = new ArrayList<Chart.ChartData>();
        Chart.ChartData data=null;
        for(int i=0;i<getLinesCount();i++){
            String name = mData.getStats().get(i).getName();
            if(mChartDataType== DataType.VIEWS){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getPageViews()/mData.getTotals().getPageViews());
            }
            result.add(data);
        }
        return result;
    }

    protected SourceLine.LineData getLineData(int position){
        final PageTitleStat data = mData.getStats().get(position);
        final PageTitleStat total = mData.getTotals();
        final PageTitleStat max = mData.getMax();
        String title = data.getName()+":";
        String value = String.valueOf(data.getPageViews());
        SourceSubline.SublineData sublineData = null;
        List<SourceSubline.SublineData> sublineDatas = new ArrayList<SourceSubline.SublineData>();
        for(int i=0;i< DataType.values().length;i++){
            if(DataType.values()[i]== DataType.VIEWS){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_views), String.valueOf(data.getPageViews()), 100f*data.getPageViews()/total.getPageViews());
            }
            sublineDatas.add(sublineData);
        }
        return new SourceLine.LineData(title, value, sublineDatas);
    }

    protected String[] getSpinnerStringsArray(){
        return new String[]{
                getResources().getString(R.string.line_views)
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

        mRequest = Webservice.getPageTitleStat(!needRefresh(), new BaseRequest.Listener<PageTitleStatResponse>() {
            @Override
            public void onResponse(PageTitleStatResponse response) {
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
