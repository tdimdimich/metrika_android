package ru.wwdi.metrika.screens.totalSummary;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.webservice.responses.AgeGenderStatResponse;
import ru.wwdi.metrika.webservice.responses.BrowsersStatResponse;
import ru.wwdi.metrika.webservice.responses.GeoStatResponse;
import ru.wwdi.metrika.webservice.responses.OSStatResponse;
import ru.wwdi.metrika.webservice.responses.SourcesSummaryStatResponse;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class TotalSummaryAdapter extends BaseAdapter {

    private TrafficSummaryStatResponse mTrafficResponse;
    private SourcesSummaryStatResponse mSourcesResponse;
    private GeoStatResponse mGeoResponse;
    private AgeGenderStatResponse mAgeGenderResponse;
    private BrowsersStatResponse mBrowsersResponse;
    private OSStatResponse mOSResponse;

    public TotalSummaryAdapter(){
    }

    public void setData(TrafficSummaryStatResponse trafficResponse,
                        SourcesSummaryStatResponse sourcesResponse,
                        GeoStatResponse geoResponse,
                        AgeGenderStatResponse ageGenderResponse,
                        BrowsersStatResponse browsersResponse,
                        OSStatResponse osResponse){
        mTrafficResponse = trafficResponse;
        mSourcesResponse = sourcesResponse;
        mGeoResponse = geoResponse;
        mAgeGenderResponse = ageGenderResponse;
        mBrowsersResponse = browsersResponse;
        mOSResponse = osResponse;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mTrafficResponse==null || mTrafficResponse.getTrafficSummaries().size()==0 ||
                mSourcesResponse==null || mSourcesResponse.getSourceSummaries().size()==0 ||
                mGeoResponse==null || mGeoResponse.getGeoStats().size()==0 ||
                mAgeGenderResponse==null || mAgeGenderResponse.getAgeStats().size()==0 || mAgeGenderResponse.getGenderStats().size()==0 ||
                mBrowsersResponse==null || mBrowsersResponse.getStats().size()==0 ||
                mOSResponse==null || mOSResponse.getStats().size()==0 ) return 0;
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position==0){
            VisitorsView vv = new VisitorsView(parent.getContext());
            vv.setData(SharedPreferencesHelper.getSelectedCounter(), mTrafficResponse);
            return vv;
        }else if(position==1){
            TimeView tv = new TimeView(parent.getContext());
            tv.setData(mTrafficResponse);
            return tv;
        }else if(position==2){
            SourcesView sv = new SourcesView(parent.getContext());
            sv.setData(mSourcesResponse);
            return sv;
        }else if(position==3){
            GeoView gv = new GeoView(parent.getContext());
            gv.setData(SharedPreferencesHelper.getSelectedCounter(), mGeoResponse);
            return gv;
        }else if(position==4){
            GenderView gv = new GenderView(parent.getContext());
            gv.setData(SharedPreferencesHelper.getSelectedCounter(), mAgeGenderResponse);
            return gv;
        }else if(position==5){
            AgeView av = new AgeView(parent.getContext());
            av.setData(SharedPreferencesHelper.getSelectedCounter(), mAgeGenderResponse);
            return av;
        }else if(position==6){
            BrowsersView bv = new BrowsersView(parent.getContext());
            bv.setData(SharedPreferencesHelper.getSelectedCounter(), mBrowsersResponse);
            return bv;
        }else if(position==7){
            OSView ov = new OSView(parent.getContext());
            ov.setData(SharedPreferencesHelper.getSelectedCounter(), mOSResponse);
            return ov;
        }
        return new View(parent.getContext());
    }
}
