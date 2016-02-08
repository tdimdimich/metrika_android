package ru.wwdi.metrika.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.screens.counterList.CounterListActivity;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.responses.CounterListResponse;

/**
 * Created by dmitrykorotchenkov on 07/04/14.
 */
public class WelcomeActivity extends Activity {

    static Integer TOKEN_REQUEST_CODE = 4589;
    static String TOKEN_RESULT_KEY = "tokenKey";

    String TAG = "Welcome Activity";

    private ProgressDialog _progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        Button enterButton = (Button) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(onEnterClick);
        _progressDialog = new ProgressDialog(this);
        _progressDialog.setMessage("Synchronizing, please wait...");
        _progressDialog.setCanceledOnTouchOutside(false);
        _progressDialog.setCancelable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TOKEN_REQUEST_CODE && resultCode == RESULT_OK) {
            loadCounters();
            YandexMetrikaApplication.sendEvent("login");
        }
    }

    private void loadCounters() {
        _progressDialog.show();
        Webservice.getCounterList(false, new BaseRequest.Listener<CounterListResponse>() {
            @Override
            public void onResponse(CounterListResponse response) {
                if(!response.hasErrors()){
                    Intent intent = new Intent(WelcomeActivity.this, CounterListActivity.class);
                    _progressDialog.dismiss();
                    WelcomeActivity.this.startActivity(intent);
                }else{
                    _progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(WelcomeActivity.this);
                    builder.setMessage(R.string.cant_get_counter_list);
                    builder.setTitle(R.string.warning);
                    builder.setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadCounters();
                        }
                    });
                    builder.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.create().show();
                }
            }
        });
    }

    View.OnClickListener onEnterClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(WelcomeActivity.this, GetTokenActivity.class);
            WelcomeActivity.this.startActivityForResult(intent, TOKEN_REQUEST_CODE);
        }
    };
}
