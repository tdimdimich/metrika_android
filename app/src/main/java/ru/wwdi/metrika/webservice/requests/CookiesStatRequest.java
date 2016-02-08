package ru.wwdi.metrika.webservice.requests;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.CookiesStat;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.responses.CookiesStatResponse;

/**
* Created with IntelliJ IDEA.
* User: vlad
* Date: 9/3/13
* Time: 10:45 AM
* To change this template use File | Settings | File Templates.
*/
public class CookiesStatRequest extends BaseRequest<CookiesStatResponse>{

    private volatile Date mStartDate;
    private volatile Date mEndDate;

// http://api-metrika.wwdi.ru/stat/stat/geo.json?id=2138128&pretty=1&oauth_token=05dd3dd84ff948fdae2bc4fb91f13e22&date1=%s&date2=dateTo";
    private static final String URL = Webservice.API_BASE_URL+"/stat/tech/cookies.json";

    public CookiesStatRequest(boolean useCache, Listener<CookiesStatResponse> listener) {
        DateInterval dateInterval = SharedPreferencesHelper.getCurrentDateInterval();
        mStartDate = dateInterval.getStartDate();
        mEndDate = dateInterval.getEndDate();
        init(useCache, RequestHelper.RequestMethod.GET, URL, new HashMap<String, String>() {
                {
                    put(API_TOKEN_PARAM_KEY, SharedPreferencesHelper.getToken());
                    put("id", String.valueOf(SharedPreferencesHelper.getSelectedCounter().getCounterId()));
                    put("date1", DateHelper.getStringFromDate(mStartDate));
                    put("date2", DateHelper.getStringFromDate(mEndDate));
    //                put("pretty", "1");
                }
            }, CookiesStatResponse.class, listener);
    }


    @Override
    protected CookiesStatResponse getCachedResponse() {
        return new Select().from(CookiesStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
    }

    @Override
    protected void removeResponseFromCache() {
        CookiesStatResponse response = new Select().from(CookiesStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
        if(response==null) return;
        CookiesStat max = response.getMax();
        CookiesStat min = response.getMin();
        CookiesStat totals = response.getTotals();
        List<CookiesStat> items = response.getStats();
        for(CookiesStat item: items){
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
    protected void beforeResponseDispatching(CookiesStatResponse response) {
        response.setStartDate(mStartDate);
        response.setEndDate(mEndDate);
    }

}
