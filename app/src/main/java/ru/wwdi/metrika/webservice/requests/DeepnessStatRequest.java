package ru.wwdi.metrika.webservice.requests;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.models.DeepnessStat;
import ru.wwdi.metrika.models.TimeStat;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.responses.DeepnessStatResponse;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class DeepnessStatRequest extends BaseRequest<DeepnessStatResponse> {

    private volatile Date mStartDate;
    private volatile Date mEndDate;

    // http://api-metrika.wwdi.ru/stat/traffic/summary.json?id=2138128&pretty=1&oauth_token=05dd3dd84ff948fdae2bc4fb91f13e22&date1=%s&date2=dateTo";
    private static final String URL = Webservice.API_BASE_URL + "/stat/traffic/deepness.json";

    public DeepnessStatRequest(boolean useCache, Listener<DeepnessStatResponse> listener) {
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
        }, DeepnessStatResponse.class, listener);
    }


    @Override
    protected DeepnessStatResponse getCachedResponse() {
        return new Select().from(DeepnessStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
    }

    @Override
    protected void removeResponseFromCache() {
        DeepnessStatResponse response = new Select().from(DeepnessStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
        if (response == null) return;
        DeepnessStat max = response.getMax();
        DeepnessStat min = response.getMin();
        DeepnessStat totals = response.getTotals();
        List<DeepnessStat> items = response.getDeepnessStats();
        for (DeepnessStat item : items) {
            item.delete();
        }
        List<TimeStat> itemsT = response.getTimeStats();
        for (TimeStat item : itemsT) {
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
    protected void beforeResponseDispatching(DeepnessStatResponse response) {
        response.setStartDate(mStartDate);
        response.setEndDate(mEndDate);
    }
}
