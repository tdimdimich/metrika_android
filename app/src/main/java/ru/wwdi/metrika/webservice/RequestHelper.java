package ru.wwdi.metrika.webservice;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.models.DataError;

/**
 * Created with IntelliJ IDEA.
 * User: ryashentsev
 * Date: 17.12.12
 * Time: 19:09
 * To change this template use File | Settings | File Templates.
 */
public class RequestHelper {

    private static final List<RequestHelper> mRequestHelpers = new ArrayList<RequestHelper>();

    public static enum RequestMethod{
        GET,
        POST,
        PUT,
        DELETE,
        REQUEST_METHOD_POST_WITHOUT_PARAMETERS_KEYS
    }

    private RequestTask requestTask;

    public RequestHelper(String url, Map<String, String> parameters, RequestMethod requestMethod, RequestHelperListener listener){
        requestTask = new RequestTask(url, parameters, requestMethod, listener, this);
    }

    public RequestHelper(String url, List<NameValuePair> parameters, RequestMethod requestMethod, RequestHelperListener listener){
        requestTask = new RequestTask(url, parameters, requestMethod, listener, this);
    }

    public RequestHelper(String url, String postBody, RequestMethod requestMethod, RequestHelperListener listener){
        requestTask = new RequestTask(url, postBody, requestMethod, listener, this);
    }

    public void start(){
        mRequestHelpers.add(this);
        requestTask.execute();
    }

    public void stop(){
        mRequestHelpers.remove(this);
        requestTask.cancel(true);
    }

    private static class RequestTask extends AsyncTask<Void,Void,Void>{
        private String url;
        private List<NameValuePair> parameters;
        private String postBody;
        private RequestMethod requestMethod;
        private RequestHelperListener listener;
        private RequestHelper helper;

        public RequestTask(String url, List<NameValuePair> parameters, RequestMethod requestMethod, RequestHelperListener listener, RequestHelper helper){
            this.url = url;
            this.parameters = parameters;
            this.requestMethod = requestMethod;
            this.listener = listener;
            this.helper = helper;
        }

        public RequestTask(String url, Map<String,String> parameters, RequestMethod requestMethod, RequestHelperListener listener, RequestHelper helper){
            this(url, getNameValuePairsFromMap(parameters), requestMethod, listener, helper);
        }

        public RequestTask(String url, String postBody, RequestMethod requestMethod, RequestHelperListener listener, RequestHelper helper){
            this.url = url;
            this.postBody = postBody;
            this.requestMethod = requestMethod;
            this.listener = listener;
            this.helper = helper;
        }

        private static List<NameValuePair> getNameValuePairsFromMap(Map<String,String> params){
            if(params==null) return null;
            Iterator<String> iterator = params.keySet().iterator();
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            NameValuePair pair;
            String key;
            while(iterator.hasNext()){
                key = iterator.next();
                pair = new BasicNameValuePair(key, params.get(key));
                nameValuePairs.add(pair);
            }
            return nameValuePairs;
        }

        private String addParamsToUrl(List<NameValuePair> params, String url){
            if(params==null || params.size()==0) return url;
            if(!url.endsWith("?"))
                url += "?";
            List<NameValuePair> nameValuePairs = params;
            if(nameValuePairs!=null){
                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                url += paramString;
            }
            return url;
        }

        private HttpEntityEnclosingRequestBase addParametersToPostRequest(HttpEntityEnclosingRequestBase postRequest, List<NameValuePair> params) throws UnsupportedEncodingException {
            List<NameValuePair> nameValuePairs = params;
            if(nameValuePairs!=null){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "utf-8");
                entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded"));
                entity.setContentEncoding("utf-8");
                postRequest.setEntity(entity);
            }
            return postRequest;
        }

        private HttpEntityEnclosingRequestBase addPostBody(HttpEntityEnclosingRequestBase postRequest, String postBody) throws UnsupportedEncodingException {
            StringEntity e = new StringEntity(postBody, "utf-8");
            postRequest.setEntity(e);
            return postRequest;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(isCancelled()){
                if(listener!=null) listener.dispatchCancel(helper);
                return null;
            }
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 10000);

            DefaultHttpClient client = new DefaultHttpClient(httpParameters);

            HttpRequestBase request = null;

            InputStream in = null;
            try{
                if(requestMethod==RequestMethod.GET){
                    url = addParamsToUrl(parameters, url);
                    request = new HttpGet();
                }else if(requestMethod==RequestMethod.POST){
                    request = new HttpPost();
                    addParametersToPostRequest((HttpPost)request, parameters);
                }else if(requestMethod==RequestMethod.PUT){
                    request = new HttpPut();
                    addParametersToPostRequest((HttpEntityEnclosingRequestBase)request, parameters);
                }else if(requestMethod==RequestMethod.DELETE){
                    url = addParamsToUrl(parameters, url);
                    request = new HttpDelete();
                }else if(requestMethod==RequestMethod.REQUEST_METHOD_POST_WITHOUT_PARAMETERS_KEYS){
                    request = new HttpPost();
                    addPostBody((HttpPost) request, postBody);
                }else{
                    return null;
                }

                int code;
//                request.setHeader("Content-Type", "application/x-www-form-urlencoded");
                request.setURI(new URI(url));

                Log.d(YandexMetrikaApplication.class.getSimpleName(), "send request to "+url+". Parameters: "+parameters);

                HttpResponse response = client.execute(request);
                code = response.getStatusLine().getStatusCode();
                if(isCancelled()){
                    if(listener!=null) listener.dispatchCancel(helper);
                    return null;
                }
                if(listener!=null) listener.dispatchStatus(code, helper);
                HttpEntity entity = response.getEntity();

                in = entity.getContent();
                String resultString = null;
                if(in!=null){
                    InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder data = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.append(line);
                    }
                    resultString = data.toString();

                    if(isCancelled()){
                        if(listener!=null) listener.dispatchCancel(helper);
                        return null;
                    }
                }
                if(listener!=null) listener.dispatchResponse(code, resultString, helper);
            }catch(IOException e){
                Log.e(YandexMetrikaApplication.class.getSimpleName(), "Can't send request", e);
                if(isCancelled()){
                    if(listener!=null) listener.dispatchCancel(helper);
                    return null;
                }
                if(listener!=null) listener.dispatchError(helper, DataError.ERROR_NO_CONNECTION);
            }catch(Exception e){
                Log.e(YandexMetrikaApplication.class.getSimpleName(), "Can't send request", e);
                if(isCancelled()){
                    if(listener!=null) listener.dispatchCancel(helper);
                    return null;
                }
                if(listener!=null) listener.dispatchError(helper, DataError.ERROR_UNKNOWN);
            }finally {
                try{
                    if(in!=null) in.close();
                }catch(IOException e){
                    Log.e(YandexMetrikaApplication.class.getSimpleName(), "Error", e);
                }
            }
            return null;
        }
    }

    public static abstract class RequestHelperListener{
        private Handler handler;

        public RequestHelperListener(){
            handler = new Handler(Looper.getMainLooper());
        }

        private void dispatchResponse(final int status, final String responseText, final RequestHelper helper){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mRequestHelpers.remove(this);
                    onResponse(status, responseText, helper);
                }
            });
        }

        private void dispatchStatus(final int status, final RequestHelper helper){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onStatus(status, helper);
                }
            });
        }

        private void dispatchError(final RequestHelper helper, final DataError error){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onError(helper, error);
                    mRequestHelpers.remove(this);
                }
            });
        }

        private void dispatchCancel(final RequestHelper helper){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mRequestHelpers.remove(this);
                    onCancel(helper);
                }
            });
        }

        public abstract void onError(RequestHelper helper, DataError error);
        public abstract void onStatus(int status, RequestHelper helper);
        public abstract void onResponse(int status, String responseText, RequestHelper helper);
        public abstract void onCancel(RequestHelper helper);
    }



}
