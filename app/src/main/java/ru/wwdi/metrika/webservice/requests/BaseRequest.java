package ru.wwdi.metrika.webservice.requests;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.DataError;
import ru.wwdi.metrika.webservice.RequestHelper;
import ru.wwdi.metrika.webservice.responses.BaseResponse;

/**
* Created with IntelliJ IDEA.
* User: ryashentsev
* Date: 17.05.13
* Time: 14:17
* To change this template use File | Settings | File Templates.
*/
public abstract class BaseRequest<T extends BaseResponse> implements Serializable{

    private Executor mExecutor = Executors.newSingleThreadExecutor();

    private static final Gson sGson = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setDateFormat("yyyyMMdd")
            .create();

    static public final String API_TOKEN_PARAM_KEY = "oauth_token";

    private RequestHelper.RequestMethod mRequestMethod;
    private volatile String mUrl;
    private volatile Map<String,String> mParams;
    private volatile RequestHelper mRequestHelper;
    private Listener<T> mListener;
    private volatile Class<T> mResponseClass;
    private volatile boolean mUseCache;
    private volatile Handler mCallbackHandler;

    public BaseRequest(){
    }

    protected void init(boolean useCache, RequestHelper.RequestMethod requestMethod, String url, Map<String, String> params, Class<T> responseClass, Listener<T> listener){
        mUseCache = useCache;
        mRequestMethod = requestMethod;
        mUrl = url;
        mParams = params;
        mResponseClass = responseClass;
        mListener = listener;
        if(listener==null || mUrl==null) throw new RuntimeException("Url, listener and response objects must not be null!");
        addLanguageParameter();
    }

    private void addLanguageParameter(){
        if(mParams==null) mParams = new HashMap<String, String>();
        Locale current = YandexMetrikaApplication.getInstance().getResources().getConfiguration().locale;
        String lang = current.getLanguage().equals("ru")?"ru":"en";
        mParams.put("lang", lang);
    }

    private void dispatchResponse(final T response){
        mCallbackHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mListener!=null) mListener.onResponse(response);
            }
        });
    }

    protected abstract T getCachedResponse();

    protected abstract void removeResponseFromCache();

    private void _start(){
        RequestHelper.RequestHelperListener listener = new RequestHelper.RequestHelperListener(){
            @Override
            public void onError(RequestHelper helper, DataError error) {
                T response = sGson.fromJson("{}", mResponseClass);
                response.setError(error);
                dispatchResponse(response);
                mRequestHelper = null;
            }

            @Override
            public void onStatus(int status, RequestHelper helper) {
            }

            @Override
            public void onResponse(int status, String responseText, RequestHelper helper) {
                try{
                    Log.d(YandexMetrikaApplication.class.getSimpleName(), "response received:\n" + responseText);
                    T response = sGson.fromJson(responseText, mResponseClass);
                    beforeResponseDispatching(response);
                    response.afterInitFromWeb();
                    dispatchResponse(response);
                    mRequestHelper = null;
                }catch (Exception e){
                    Log.e(YandexMetrikaApplication.class.getSimpleName(), "Can't parse response "+ mResponseClass.getSimpleName(), e);
                    onError(helper, DataError.ERROR_CANT_PARSE_JSON);
                }
            }

            @Override
            public void onCancel(RequestHelper helper) {
                mRequestHelper = null;
            }
        };
        mRequestHelper = new RequestHelper(mUrl, mParams, mRequestMethod, listener);
        mRequestHelper.start();
    }

    public void start(){
        mCallbackHandler = new Handler();
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if(mUseCache && !DateHelper.getTodayDate().equals(SharedPreferencesHelper.getCurrentDateInterval().getEndDate())){
                    T response = getCachedResponse();
                    if(response!=null){
                        dispatchResponse(response);
                        return;
                    }
                }
                removeResponseFromCache();
                _start();
            }
        });
    }

    /**
     * Response object may be modified before dispatching it to webservice listener.
     * This method invoked before afterInitFromWeb method of response object
     * @param response
     */
    protected abstract void beforeResponseDispatching(T response);

    public void stop(){
        if(mRequestHelper!=null) mRequestHelper.stop();
    }

    public static interface Listener<T extends BaseResponse>{
        public void onResponse(T response);
    }
}
