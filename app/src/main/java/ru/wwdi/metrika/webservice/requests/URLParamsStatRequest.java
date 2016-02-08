package ru.wwdi.metrika.webservice.requests;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.models.URLParamsStat;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.responses.URLParamsStatResponse;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class URLParamsStatRequest extends BaseRequest<URLParamsStatResponse> {

    private volatile Date mStartDate;
    private volatile Date mEndDate;

    private static final String URL = Webservice.API_BASE_URL + "/stat/content/url_param.json";

    public URLParamsStatRequest(boolean useCache, Listener<URLParamsStatResponse> listener) {
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
        }, URLParamsStatResponse.class, listener);
    }


    @Override
    protected URLParamsStatResponse getCachedResponse() {
        return new Select().from(URLParamsStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
    }

    @Override
    protected void removeResponseFromCache() {
        URLParamsStatResponse response = new Select().from(URLParamsStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
        if (response == null) return;
        URLParamsStat max = response.getMax();
        URLParamsStat min = response.getMin();
        URLParamsStat totals = response.getTotals();
        List<URLParamsStat> items = response.getStats();
        for (URLParamsStat item : items) {
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
    protected void beforeResponseDispatching(URLParamsStatResponse response) {
        response.setStartDate(mStartDate);
        response.setEndDate(mEndDate);
    }
}
