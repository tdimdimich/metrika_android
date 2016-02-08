package ru.wwdi.metrika.screens;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;

public class GetTokenActivity extends Activity {

    private static final String TAG = "GetTokenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_token);
        WebView web = (WebView) findViewById(R.id.web);
        String url = "https://oauth.yandex.ru/authorize?response_type=token&client_id=" + YandexMetrikaApplication.APP_ID;

        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "WebView:shouldOverrideUrlLoading   " + url);
                String tokenKeyString = "#access_token=";
                if ((url.indexOf("metrikatest")) >= 0 && (url.indexOf(tokenKeyString)) >= 0) {
                    int loc = url.indexOf(tokenKeyString) + tokenKeyString.length();
                    String token = url.substring(loc, url.indexOf("&", loc));
                    SharedPreferencesHelper.setToken(token);
                    setResult(RESULT_OK);
                    finish();
                }
                return false;
            }
        });
        Log.e(TAG, "loadUrl   " + url);

        web.loadUrl(url);
    }
}

