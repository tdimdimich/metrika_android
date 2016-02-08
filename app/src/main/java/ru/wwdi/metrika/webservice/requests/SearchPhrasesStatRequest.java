package ru.wwdi.metrika.webservice.requests;

import com.activeandroid.query.Select;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.models.SearchPhraseStat;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.responses.SearchPhrasesStatResponse;

/**
* Created with IntelliJ IDEA.
* User: vlad
* Date: 9/3/13
* Time: 10:45 AM
* To change this template use File | Settings | File Templates.
*/
public class SearchPhrasesStatRequest extends BaseRequest<SearchPhrasesStatResponse>{

    private volatile Date mStartDate;
    private volatile Date mEndDate;

    private static final String URL = Webservice.API_BASE_URL+"/stat/sources/phrases.json";

    public SearchPhrasesStatRequest(boolean useCache, Listener<SearchPhrasesStatResponse> listener) {
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
            }, SearchPhrasesStatResponse.class, listener);
    }


    @Override
    protected SearchPhrasesStatResponse getCachedResponse() {
        return new Select().from(SearchPhrasesStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
    }

    @Override
    protected void removeResponseFromCache() {
        SearchPhrasesStatResponse response = new Select().from(SearchPhrasesStatResponse.class).where("startDate='" + mStartDate.getTime() + "'").and("endDate='" + mEndDate.getTime() + "'").executeSingle();
        if(response==null) return;
        SearchPhraseStat max = response.getMax();
        SearchPhraseStat min = response.getMin();
        SearchPhraseStat totals = response.getTotals();
        List<SearchPhraseStat> items = response.getSearchPhraseStats();
        for(SearchPhraseStat item: items){
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
        ;
    }

    @Override
    protected void beforeResponseDispatching(SearchPhrasesStatResponse response) {
        response.setStartDate(mStartDate);
        response.setEndDate(mEndDate);
    }
}
