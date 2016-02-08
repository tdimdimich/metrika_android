package ru.wwdi.metrika.webservice.requests;

import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.HashMap;

import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.responses.CounterListResponse;

/**
* Created with IntelliJ IDEA.
* User: vlad
* Date: 9/3/13
* Time: 10:45 AM
* To change this template use File | Settings | File Templates.
*/
@Table(name = "CounterListResponse")
public class CounterListRequest extends BaseRequest<CounterListResponse>{

    private static final String URL = Webservice.API_BASE_URL+"/counters.json";

    public CounterListRequest(boolean useCache, BaseRequest.Listener<CounterListResponse> listener) {
        super();
        init(useCache, RequestHelper.RequestMethod.GET,
                URL,
                new HashMap<String, String>(){
                    {
                        put(API_TOKEN_PARAM_KEY, SharedPreferencesHelper.getToken());
                    }
                },
                CounterListResponse.class,
                listener);
    }

    @Override
    protected CounterListResponse getCachedResponse() {
        return new Select().from(CounterListResponse.class).executeSingle();
    }

    @Override
    protected void removeResponseFromCache() {
//        List<CounterListResponse> list = new Select().from(CounterListResponse.class).execute();
//        for (CounterListResponse response : list) {
//            for (Counter counter : response.getCounters()) {
//                counter.delete();
//            }
//            response.delete();
//        }
    }

    @Override
    protected void beforeResponseDispatching(CounterListResponse response) {

    }
}
