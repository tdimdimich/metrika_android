package ru.wwdi.metrika.screens.totalSummary;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.screens.BaseScreen;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.AgeGenderStatRequest;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.requests.BrowsersStatRequest;
import ru.wwdi.metrika.webservice.requests.GeoStatRequest;
import ru.wwdi.metrika.webservice.requests.OSStatRequest;
import ru.wwdi.metrika.webservice.requests.SourcesSummaryStatRequest;
import ru.wwdi.metrika.webservice.requests.TrafficSummaryStatRequest;
import ru.wwdi.metrika.webservice.responses.AgeGenderStatResponse;
import ru.wwdi.metrika.webservice.responses.BrowsersStatResponse;
import ru.wwdi.metrika.webservice.responses.GeoStatResponse;
import ru.wwdi.metrika.webservice.responses.OSStatResponse;
import ru.wwdi.metrika.webservice.responses.SourcesSummaryStatResponse;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 07.05.14.
 */
public class TotalSummaryScreen extends BaseScreen {

    private TrafficSummaryStatRequest mTrafficRequest;
    private SourcesSummaryStatRequest mSourcesRequest;
    private GeoStatRequest mGeoRequest;
    private AgeGenderStatRequest mAgeGenderRequest;
    private BrowsersStatRequest mBrowsersRequest;
    private OSStatRequest mOSRequest;

    private TrafficSummaryStatResponse mTrafficResponse;
    private SourcesSummaryStatResponse mSourcesResponse;
    private GeoStatResponse mGeoResponse;
    private AgeGenderStatResponse mAgeGenderResponse;
    private BrowsersStatResponse mBrowsersResponse;
    private OSStatResponse mOSResponse;

    private TotalSummaryAdapter mAdapter;
    private boolean mNeedRefresh = false;

    public TotalSummaryScreen() {
        mAdapter = new TotalSummaryAdapter();
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                ListView listView = getListView();
                if(listView!=null){
                    MotionEvent me = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0);
                    listView.dispatchTouchEvent(me);
                    me.recycle();
                }
                super.onChanged();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final SwipeRefreshLayout view = (SwipeRefreshLayout) inflater.inflate(R.layout.total_summary, null);
        view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullDownRefresh();
                view.setRefreshing(true);
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected void showContent() {
        super.showContent();
        View parent = (View) getView().getParent();
        Log.d(YandexMetrikaApplication.class.getSimpleName(), "parent = "+parent);
        mAdapter.setData(mTrafficResponse, mSourcesResponse, mGeoResponse, mAgeGenderResponse, mBrowsersResponse, mOSResponse);
    }

    private boolean isLoaded(){
        return mTrafficResponse!=null &&
                mSourcesResponse!=null &&
                mGeoResponse!=null &&
                mAgeGenderResponse!=null &&
                mBrowsersResponse!=null &&
                mOSResponse!=null;
    }

    @Override
    public void onResume() {
        SwipeRefreshLayout srl = (SwipeRefreshLayout)getView().findViewById(R.id.swipeRefreshLayout);
        srl.setRefreshing(false);
        srl.setEnabled(true);
        if(isLoaded()){
            showContent();
            setListViewEnabled(true);
        }else{
            showLoading();
            load();
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

    private ListView getListView(){
        if(getView()!=null){
            return (ListView) getView().findViewById(R.id.listView);
        }
        return null;
    }

    private void setListViewEnabled(boolean enabled){
        ListView listView = getListView();
        if(listView!=null){
            listView.setEnabled(enabled);
        }
    }

    private void load(){
        SwipeRefreshLayout srl = (SwipeRefreshLayout)getView().findViewById(R.id.swipeRefreshLayout);
        srl.setEnabled(false);
        setListViewEnabled(false);

        mTrafficRequest = Webservice.getTrafficSummaryStat(!mNeedRefresh, new BaseRequest.Listener<TrafficSummaryStatResponse>() {
            @Override
            public void onResponse(TrafficSummaryStatResponse response) {
                if (response.hasErrors()) {
                    showError();
                }else{
                    mTrafficResponse = response;
                    checkLoaded();
                }
            }
        });
        mSourcesRequest = Webservice.getSourcesSummaryStat(!mNeedRefresh, new BaseRequest.Listener<SourcesSummaryStatResponse>() {
            @Override
            public void onResponse(SourcesSummaryStatResponse response) {
                if (response.hasErrors()) {
                    showError();
                }else{
                    mSourcesResponse = response;
                    checkLoaded();
                }
            }
        });
        mGeoRequest = Webservice.getGeoStat(!mNeedRefresh, new BaseRequest.Listener<GeoStatResponse>() {
            @Override
            public void onResponse(GeoStatResponse response) {
                if (response.hasErrors()) {
                    showError();
                }else{
                    mGeoResponse = response;
                    checkLoaded();
                }
            }
        });
        mAgeGenderRequest = Webservice.getAgeGenderStat(!mNeedRefresh, new BaseRequest.Listener<AgeGenderStatResponse>() {
            @Override
            public void onResponse(AgeGenderStatResponse response) {
                if (response.hasErrors()) {
                    showError();
                }else{
                    mAgeGenderResponse = response;
                    checkLoaded();
                }
            }
        });
        mBrowsersRequest = Webservice.getBrowsersStat(!mNeedRefresh, new BaseRequest.Listener<BrowsersStatResponse>() {
            @Override
            public void onResponse(BrowsersStatResponse response) {
                if (response.hasErrors()) {
                    showError();
                }else{
                    mBrowsersResponse = response;
                    checkLoaded();
                }
            }
        });
        mOSRequest = Webservice.getOSStat(!mNeedRefresh, new BaseRequest.Listener<OSStatResponse>() {
            @Override
            public void onResponse(OSStatResponse response) {
                if (response.hasErrors()) {
                    showError();
                }else{
                    mOSResponse = response;
                    checkLoaded();
                }
            }
        });
        mNeedRefresh = !mNeedRefresh;
    }

    private void checkLoaded(){
        if(isLoaded() && isResumed()){
            setListViewEnabled(true);
            SwipeRefreshLayout srl = (SwipeRefreshLayout)getView().findViewById(R.id.swipeRefreshLayout);
            srl.setRefreshing(false);
            srl.setEnabled(true);
            showContent();
        }
    }

    private void reset(){
        if(mTrafficRequest!=null) mTrafficRequest.stop();
        if(mSourcesRequest!=null) mSourcesRequest.stop();
        if(mGeoRequest!=null) mGeoRequest.stop();
        if(mAgeGenderRequest!=null) mAgeGenderRequest.stop();
        if(mBrowsersRequest!=null) mBrowsersRequest.stop();
        if(mOSRequest!=null) mOSRequest.stop();

        mTrafficResponse = null;
        mSourcesResponse = null;
        mGeoResponse = null;
        mAgeGenderResponse = null;
        mBrowsersResponse = null;
        mOSResponse = null;
    }

    private void pullDownRefresh(){
        mNeedRefresh = true;
        reset();
        load();
    }

    @Override
    public void refresh() {
        mNeedRefresh = true;
        reset();
        showLoading();
        if(isResumed()) load();
    }

}
