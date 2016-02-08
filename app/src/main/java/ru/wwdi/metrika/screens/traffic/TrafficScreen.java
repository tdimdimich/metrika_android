package ru.wwdi.metrika.screens.traffic;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.helpers.PxDpHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.helpers.TipsHelper;
import ru.wwdi.metrika.helpers.WaitHelper;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.screens.BaseScreen;
import ru.wwdi.metrika.screens.traffic.chart.Chart;
import ru.wwdi.metrika.views.ScrollViewWithDisablableScrolling;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.requests.TrafficSummaryStatRequest;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 12.05.14.
 */
public class TrafficScreen extends BaseScreen {

    public static enum DataType {
        VISITS,
        VIEWS,
        VISITORS,
        NEW_VISITORS,
        DENIAL,
        VIEW_DEPTH,
        VISIT_TIME
    }


    private TrafficSummaryStatRequest mRequest;
    private TrafficSummaryStatResponse mData;
    private boolean mNeedRefresh = false;
    private boolean[] mSelectedPoints = new boolean[]{true, true, true, true, true, true};
    private Chart.ChartType mChartType = Chart.ChartType.POINTS;
    private DataType mChartDataType = DataType.VISITS;

    public TrafficScreen() {

    }

    private boolean isLoaded() {
        return mData != null;
    }

    private void updateChartSize() {
        Configuration config = YandexMetrikaApplication.getInstance().getResources().getConfiguration();
        int screenWidth = PxDpHelper.dpToPx(config.screenWidthDp);
        int height;
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            height = PxDpHelper.dpToPx(config.screenHeightDp);
        } else {
            height = screenWidth * 2 / 3;
        }
        View v = getView();
        View chartContainer = v.findViewById(R.id.chartContainer);
        ViewGroup.LayoutParams lp = chartContainer.getLayoutParams();
        lp.height = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        chartContainer.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        updateChartSize();
        ScrollViewWithDisablableScrolling scrollView = (ScrollViewWithDisablableScrolling) getView().findViewById(R.id.scrollView);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            scrollView.setScrollingEnabled(false);
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        } else {
            scrollView.setScrollingEnabled(true);
        }
        super.onConfigurationChanged(newConfig);
    }

    private void initLines() {
        View v = getView();
        ViewGroup container = (ViewGroup) v.findViewById(R.id.linesContainer);

        TrafficStatLine visitsLine = (TrafficStatLine) container.findViewById(R.id.line1);
        TrafficStatLine viewLine = (TrafficStatLine) container.findViewById(R.id.line2);
        TrafficStatLine visitorsLine = (TrafficStatLine) container.findViewById(R.id.line3);
        TrafficStatLine newLine = (TrafficStatLine) container.findViewById(R.id.line4);
        TrafficStatLine denialLine = (TrafficStatLine) container.findViewById(R.id.line5);
        TrafficStatLine depthLine = (TrafficStatLine) container.findViewById(R.id.line6);
        TrafficStatLine timeLine = (TrafficStatLine) container.findViewById(R.id.line7);
        TrafficStatLine[] lines = new TrafficStatLine[]{visitsLine, viewLine, visitorsLine, newLine, denialLine, depthLine, timeLine};

        TrafficStatLine line;
        Counter counter = SharedPreferencesHelper.getSelectedCounter();
        if (mData == null) {
            showLoading();
            load();
        } else {
            for (int i = 0; i < DataType.values().length; i++) {
                line = lines[i];
                line.setData(i == 0 ? counter.getColor() : Color.BLACK, mData, mSelectedPoints, DataType.values()[i]);
                line.close(true);
            }
        }
    }

    private void initViews() {
        if (!isResumed() || !isLoaded()) return;
        final View v = getView();

        initLines();

        Spinner spinner = (Spinner) v.findViewById(R.id.chartSourceTypes);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.traffic_spinner, android.R.id.text1, new String[]{
                getResources().getString(R.string.line_visits),
                getResources().getString(R.string.line_views),
                getResources().getString(R.string.line_visitors),
                getResources().getString(R.string.line_new),
                getResources().getString(R.string.line_denial),
                getResources().getString(R.string.line_depth),
                getResources().getString(R.string.line_time)
        });
        spinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setSelection(mChartDataType.ordinal());

        final Chart chart = (Chart) v.findViewById(R.id.chart);
        chart.setData(mData);
        chart.setViewType(mChartType);
        chart.setDataType(mChartDataType);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                chart.setDataType(DataType.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final ImageButton chartTypeSwitcher = (ImageButton) v.findViewById(R.id.chartType);

        if (mChartType == Chart.ChartType.POINTS) {
            chartTypeSwitcher.setImageResource(R.drawable.traffic_graph_point);
        } else {
            chartTypeSwitcher.setImageResource(R.drawable.traffic_graph_col);
        }

        chartTypeSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mChartType == Chart.ChartType.POINTS) {
                    mChartType = Chart.ChartType.COLUMNS;
                    chartTypeSwitcher.setImageResource(R.drawable.traffic_graph_col);
                } else {
                    mChartType = Chart.ChartType.POINTS;
                    chartTypeSwitcher.setImageResource(R.drawable.traffic_graph_point);
                }
                chart.setViewType(mChartType);
            }
        });
        PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(SharedPreferencesHelper.getSelectedCounter().getColor(), PorterDuff.Mode.SRC_ATOP);
        chartTypeSwitcher.setColorFilter(colorFilter);

        chart.setSelectListener(new Chart.SelectListener() {
            @Override
            public void onSelectionChanged(boolean[] selectedPoints) {
                mSelectedPoints = selectedPoints.clone();
                boolean areAllPointsUnselected = true;
                for (boolean p : mSelectedPoints) {
                    if (p) {
                        areAllPointsUnselected = false;
                        break;
                    }
                }
                if (areAllPointsUnselected) {
                    mSelectedPoints = new boolean[]{true, true, true, true, true, true};
                }
                initLines();
            }
        });

        updateChartSize();

        ScrollViewWithDisablableScrolling scrollView = (ScrollViewWithDisablableScrolling) v.findViewById(R.id.scrollView);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            scrollView.setScrollingEnabled(false);
        } else {
            scrollView.setScrollingEnabled(true);
        }

        SwipeRefreshLayout srl = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownRefresh();
            }
        });
    }

    private void load() {
        SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        srl.setEnabled(false);

        mRequest = Webservice.getTrafficSummaryStat(!mNeedRefresh, new BaseRequest.Listener<TrafficSummaryStatResponse>() {
            @Override
            public void onResponse(TrafficSummaryStatResponse response) {
                if (response.hasErrors()) {
                    if (response.hasNoDataError()) {
                        showNoData();
                    } else {
                        showError();
                    }
                } else {
                    mData = response;
                    showContent();
                }
            }
        });
        mNeedRefresh = !mNeedRefresh;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.traffic, null);
        return v;
    }

    @Override
    protected void showContent() {
        if (!isResumed()) return;
        initViews();
        SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        srl.setEnabled(true);
        srl.setRefreshing(false);
        super.showContent();
    }

    @Override
    public void onResume() {
        WaitHelper.waitAndExecute(1000, new Runnable() {
            @Override
            public void run() {
                if (isResumed()) TipsHelper.showRotateTipIfNeeded(getActivity());
            }
        });
        SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        srl.setRefreshing(false);
        srl.setEnabled(true);
        if (isLoaded()) {
            showContent();
        } else {
            showLoading();
            load();
        }
        super.onResume();
    }

    @Override
    protected void showError() {
        super.showError();
        if (getView() != null) {
            SwipeRefreshLayout srl = (SwipeRefreshLayout) getView();
            srl.setRefreshing(false);
            srl.setEnabled(true);
        }
    }

    private void pullDownRefresh() {
        mNeedRefresh = true;
        reset();
        load();
    }

    private void reset() {
        if (mRequest != null) mRequest.stop();
        mData = null;
    }

    @Override
    public void refresh() {
        mNeedRefresh = true;
        reset();
        showLoading();
        if (isResumed()) load();
    }
}
