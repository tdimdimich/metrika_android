package ru.wwdi.metrika.screens.counterList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.screens.MainScreen;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.responses.CounterListResponse;

/**
 * Created by dmitrykorotchenkov on 14/04/14.
 */
public class CounterListActivity extends Activity {

    private CounterListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_list);
        Webservice.getCounterList(true, new BaseRequest.Listener<CounterListResponse>() {
            @Override
            public void onResponse(CounterListResponse response) {
                if (!response.hasErrors()) {
                    setCounterList(response.getCounters());
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(CounterListActivity.this);
                    builder.setMessage(R.string.no_available_counters);
                    builder.setTitle(R.string.warning);
                    builder.setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
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

    private void setCounterList(List<Counter> counters){
        if(counters.size()>0){
            SharedPreferencesHelper.setSelectedCounter(counters.get(0));
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new CounterListAdapter(this, counters);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferencesHelper.setSelectedCounter(mAdapter.getItem(position));
                mAdapter.notifyDataSetChanged();
            }
        });

        View btn = findViewById(R.id.button);
        btn.setEnabled(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextScreen();
            }
        });
    }

    private void goToNextScreen(){
        Intent intent = new Intent(this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}
