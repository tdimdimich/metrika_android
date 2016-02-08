package ru.wwdi.metrika.screens.content;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.PopularStat;
import ru.wwdi.metrika.screens.base.BaseStatisticsScreen;
import ru.wwdi.metrika.screens.base.Chart;
import ru.wwdi.metrika.screens.base.SourceLine;
import ru.wwdi.metrika.screens.base.SourceSubline;
import ru.wwdi.metrika.views.ScrollViewWithDisablableScrolling;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.requests.PopularStatRequest;
import ru.wwdi.metrika.webservice.responses.PopularStatResponse;

/**
 * Created by ryashentsev on 16.05.14.
 */
public class PopularStatScreen extends BaseStatisticsScreen {

    protected static enum DataType {
        VIEWS,
        ENTRANCE,
        EXITS
    }

    private PopularStatRequest mRequest;
    private PopularStatResponse mData;
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
            String name = mData.getStats().get(i).getUrl();
            if(mChartDataType== DataType.VIEWS){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getPageViews()/mData.getTotals().getPageViews());
            }else if(mChartDataType== DataType.ENTRANCE){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getEntrance()/mData.getTotals().getEntrance());
            }else if(mChartDataType== DataType.EXITS){
                data = new Chart.ChartData(name, 100f*mData.getStats().get(i).getExit()/mData.getMax().getExit());
            }
            result.add(data);
        }
        return result;
    }

    protected SourceLine.LineData getLineData(int position){
        final PopularStat data = mData.getStats().get(position);
        final PopularStat total = mData.getTotals();
        final PopularStat max = mData.getMax();
        String title = data.getUrl()+":";
        String value = String.valueOf(data.getPageViews());
        SourceSubline.SublineData sublineData = null;
        List<SourceSubline.SublineData> sublineDatas = new ArrayList<SourceSubline.SublineData>();
        for(int i=0;i< DataType.values().length;i++){
            if(DataType.values()[i]== DataType.VIEWS){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_views), String.valueOf(data.getPageViews()), 100f*data.getPageViews()/total.getPageViews());
            }else if(DataType.values()[i]== DataType.ENTRANCE){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_entrance), String.valueOf(data.getEntrance()), 100f*data.getEntrance()/total.getEntrance());
            }else if(DataType.values()[i]== DataType.EXITS){
                sublineData = new SourceSubline.SublineData(getString(R.string.line_exits), String.valueOf(data.getExit()), 100f*data.getExit()/total.getExit());
            }
            sublineDatas.add(sublineData);
        }
        return new SourceLine.LineData(title, value, sublineDatas);
    }

    protected String[] getSpinnerStringsArray(){
        return new String[]{
                getResources().getString(R.string.line_views),
                getResources().getString(R.string.line_entrance),
                getResources().getString(R.string.line_exits)
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

        mRequest = Webservice.getPopularStat(!needRefresh(), new BaseRequest.Listener<PopularStatResponse>() {
            @Override
            public void onResponse(PopularStatResponse response) {
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
