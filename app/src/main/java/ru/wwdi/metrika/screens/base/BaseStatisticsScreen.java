package ru.wwdi.metrika.screens.base;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.helpers.PxDpHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.helpers.TipsHelper;
import ru.wwdi.metrika.helpers.WaitHelper;
import ru.wwdi.metrika.screens.BaseScreen;
import ru.wwdi.metrika.views.ScrollViewWithDisablableScrolling;

/**
 * Created by ryashentsev on 15.05.14.
 */
public abstract class BaseStatisticsScreen extends BaseScreen {


    private boolean mNeedRefresh = false;
    public BaseStatisticsScreen(){

    }

    protected boolean needRefresh(){
        return mNeedRefresh;
    }

    protected abstract boolean isLoaded();

    private void updateChartSize(){
        View v = getView();
        View chartContainer = v.findViewById(R.id.chartContainer);
        View linesContainer = v.findViewById(R.id.linesContainer);
        View chartScrollView = v.findViewById(R.id.chartScrollView);
        SwipeRefreshLayout srl = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        Configuration config = YandexMetrikaApplication.getInstance().getResources().getConfiguration();
        int height;
        if(config.orientation==Configuration.ORIENTATION_LANDSCAPE){
            srl.setEnabled(false);
            height = PxDpHelper.dpToPx(config.screenHeightDp);
            linesContainer.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) chartScrollView.getLayoutParams();
            lp.weight=1;
            lp.height=0;
            chartScrollView.setLayoutParams(lp);
        }else{
            srl.setEnabled(true);
            height = ViewGroup.LayoutParams.WRAP_CONTENT;
            linesContainer.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) chartScrollView.getLayoutParams();
            lp.weight=0;
            lp.height=LinearLayout.LayoutParams.WRAP_CONTENT;
            chartScrollView.setLayoutParams(lp);
        }

        ViewGroup.LayoutParams lp = chartContainer.getLayoutParams();
        lp.height = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        chartContainer.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateChartSize();
        ScrollViewWithDisablableScrolling scrollView = (ScrollViewWithDisablableScrolling) getView().findViewById(R.id.scrollView);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
//            scrollView.setScrollingEnabled(false);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }else{
//            scrollView.setScrollingEnabled(true);
        }
        super.onConfigurationChanged(newConfig);
    }

    private void addLine(final ViewGroup container, final int position){
        if(!isResumed()) return;
        int color = position==0?SharedPreferencesHelper.getSelectedCounter().getColor():Color.BLACK;
        SourceLine line = new SourceLine(getActivity());
        line.setData(color, getLineData(position));
        line.close(true);
        container.addView(line);
        AlphaAnimation a = new AlphaAnimation(0, 1);
        a.setDuration(200);
        line.startAnimation(a);
    }

    protected abstract SourceLine.LineData getLineData(int position);

    protected abstract int getLinesCount();

    private void initLines(){
        View v = getView();
        final ViewGroup container = (ViewGroup) v.findViewById(R.id.linesContainer);
        container.removeAllViews();

        int linesCount = getLinesCount();
        for(int i=0;i<linesCount;i++){
            addLine(container, i);
        }
    }

    private void setChartData(){
        if(!isLoaded()) return;
        final Chart chart = (Chart) getView().findViewById(R.id.chart);
        chart.setData(getChartData());
    }

    protected abstract List<Chart.ChartData> getChartData();
    protected abstract String[] getSpinnerStringsArray();
    protected abstract void onSpinnerItemSelected(int position);

    private void initViews(){
        if(!isResumed() || !isLoaded()) return;
        final View v = getView();

        initLines();

        Spinner spinner = (Spinner) v.findViewById(R.id.chartSourceTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.traffic_spinner, android.R.id.text1, getSpinnerStringsArray());
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                parent.post(new Runnable() {
                    @Override
                    public void run() {
                        onSpinnerItemSelected(position);
                        setChartData();
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner.setSelection(getSpinnerSelectedPosition());

//        setChartData();

        ScrollViewWithDisablableScrolling scrollView = (ScrollViewWithDisablableScrolling) v.findViewById(R.id.scrollView);
        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE){
            scrollView.setScrollingEnabled(false);
        }else{
            scrollView.setScrollingEnabled(true);
        }

        SwipeRefreshLayout srl = (SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownRefresh();
            }
        });
    }

    protected abstract int getSpinnerSelectedPosition();

    protected abstract void load();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.statistics_screen, null);
        return v;
    }

    @Override
    protected void showContent() {
        if(!isResumed()) return;
        initViews();
        SwipeRefreshLayout srl = (SwipeRefreshLayout)getView().findViewById(R.id.swipeRefreshLayout);
        srl.setEnabled(true);
        srl.setRefreshing(false);
        super.showContent();
    }

    @Override
    public void onResume() {
        WaitHelper.waitAndExecute(1000, new Runnable() {
            @Override
            public void run() {
                if(isResumed()) TipsHelper.showRotateTipIfNeeded(getActivity());
            }
        });
        SwipeRefreshLayout srl = (SwipeRefreshLayout)getView().findViewById(R.id.swipeRefreshLayout);
        srl.setRefreshing(false);
        srl.setEnabled(true);
        if(isLoaded()){
            showContent();
        }else{
            showLoading();
            load();
            mNeedRefresh = false;
        }
        super.onResume();
    }

    @Override
    protected void showError() {
        super.showError();
        if(getView()!=null){
            SwipeRefreshLayout srl = (SwipeRefreshLayout)getView().findViewById(R.id.swipeRefreshLayout);
            srl.setRefreshing(false);
            srl.setEnabled(true);
        }
    }

    private void pullDownRefresh(){
        mNeedRefresh = true;
        reset();
        load();
        mNeedRefresh = false;
    }

    protected abstract void reset();

    @Override
    public void refresh() {
        mNeedRefresh = true;
        reset();
        showLoading();
        if(isResumed()){
            load();
            mNeedRefresh = false;
        }
    }
}
