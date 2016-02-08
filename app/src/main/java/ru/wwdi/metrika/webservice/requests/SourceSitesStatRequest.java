package ru.wwdi.metrika.webservice.requests;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.models.SourceSitesStat;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.responses.SourceSitesStatResponse;

/**
 * Created with IntelliJ IDEA.
 * User: vlad
 * Date: 9/3/13
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class SourceSitesStatRequest extends BaseRequest<SourceSitesStatResponse> {

    private volatile Date mStartDate;
    private volatile Date mEndDate;

    private static final String URL = Webservice.API_BASE_URL + "/stat/sources/sites.json";

    public SourceSitesStatRequest(boolean useCache, Listener<SourceSitesStatResponse> listener) {
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
        }, SourceSitesStatResponse.class, listener);
    }


    @Override
    protected SourceSitesStatResponse getCachedResponse() {
        return new Select().from(SourceSitesStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
    }

    @Override
    protected void removeResponseFromCache() {
        SourceSitesStatResponse response = new Select().from(SourceSitesStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
        if (response == null) return;
        SourceSitesStat max = response.getMax();
        SourceSitesStat min = response.getMin();
        SourceSitesStat totals = response.getTotals();
        List<SourceSitesStat> items = response.getSourceSiteStats();
        for (SourceSitesStat item : items) {
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
    protected void beforeResponseDispatching(SourceSitesStatResponse response) {
        response.setStartDate(mStartDate);
        response.setEndDate(mEndDate);
    }
}
