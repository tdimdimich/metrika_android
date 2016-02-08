package ru.wwdi.metrika.webservice.requests;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.models.SilverlightStat;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.responses.SilverlightStatResponse;

/**
* Created with IntelliJ IDEA.
* User: vlad
* Date: 9/3/13
* Time: 10:45 AM
* To change this template use File | Settings | File Templates.
*/
public class SilverlightStatRequest extends BaseRequest<SilverlightStatResponse>{

    private volatile Date mStartDate;
    private volatile Date mEndDate;

// http://api-metrika.wwdi.ru/stat/stat/geo.json?id=2138128&pretty=1&oauth_token=05dd3dd84ff948fdae2bc4fb91f13e22&date1=%s&date2=dateTo";
    private static final String URL = Webservice.API_BASE_URL+"/stat/tech/silverlight.json";

    public SilverlightStatRequest(boolean useCache, Listener<SilverlightStatResponse> listener) {
        DateInterval dateInterval = SharedPreferencesHelper.getCurrentDateInterval();
        mStartDate = dateInterval.getStartDate();
        mEndDate = dateInterval.getEndDate();
        init(useCache, RequestHelper.RequestMethod.GET, URL, new HashMap<String, String>() {
                {
                    put(API_TOKEN_PARAM_KEY, SharedPreferencesHelper.getToken());
                    put("id", String.valueOf(SharedPreferencesHelper.getSelectedCounter().getCounterId()));
                    put("date1", DateHelper.getStringFromDate(mStartDate));
                    put("date2", DateHelper.getStringFromDate(mEndDate));
                    put("table_mode", "tree");
    //                put("pretty", "1");
                }
            }, SilverlightStatResponse.class, listener);
    }


    @Override
    protected SilverlightStatResponse getCachedResponse() {
        return new Select().from(SilverlightStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
    }

    @Override
    protected void removeResponseFromCache() {
        SilverlightStatResponse response = new Select().from(SilverlightStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
        if(response==null) return;
        SilverlightStat max = response.getMax();
        SilverlightStat min = response.getMin();
        SilverlightStat totals = response.getTotals();
        List<SilverlightStat> items = response.getStats();
        for(SilverlightStat item: items){
            item.delete();
        }
        response.delete();

        if (max != null) {
            max.delete();
        }
        if (min != null) {
            min.delete();
        }
        if (totals != null) {
            totals.delete();
        }
    }

    @Override
    protected void beforeResponseDispatching(SilverlightStatResponse response) {
        response.setStartDate(mStartDate);
        response.setEndDate(mEndDate);
    }

}
