package ru.wwdi.metrika.screens.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.screens.BaseScreen;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.responses.CounterListResponse;

/**
 * Created by ryashentsev on 04.05.14.
 */
public class Settings1Screen extends BaseScreen implements CounterListItemView.CounterListItemViewListener {

    private CounterListAdapter mAdapter;
    private SettingsListener mListener;

    public void setListener(SettingsListener listener){
        mListener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings1, null);
        v.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCounters();
                if (mListener != null) {
                    mListener.onSettingsUpdated();
                }
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCounters();
    }



    private void resetCounters(){
        Webservice.getCounterList(true, new BaseRequest.Listener<CounterListResponse>() {
            @Override
            public void onResponse(CounterListResponse response) {
                //nothing
            }
        });
    }

    private void saveCounters(){
        for(int i=0;i<mAdapter.getCount();i++){
            mAdapter.getItem(i).save();
        }
    }

    private void getCounters(){
        Webservice.getCounterList(true, new BaseRequest.Listener<CounterListResponse>() {
            @Override
            public void onResponse(CounterListResponse response) {
                if (!response.hasErrors()) {
                    mAdapter = new CounterListAdapter(Settings1Screen.this, response.getCounters());
                    if(isResumed()){
                        ((ListView)getView().findViewById(R.id.countersList)).setAdapter(mAdapter);
                    }
                }
            }
        });
    }

    @Override
    public void onVisibilityClick(final CounterListItemView view) {
        final Counter counter = view.getCounter();
        counter.setVisible(!counter.isVisible());
        view.setCounter(counter, true);
        getView().findViewById(R.id.confirm).setEnabled(hasVisibleCounters());
    }

    private boolean hasVisibleCounters(){
        for(int i=0;i<mAdapter.getCount();i++){
            if(mAdapter.getItem(i).isVisible()) return true;
        }
        return false;
    }

    @Override
    public void onSettingsClick(CounterListItemView view) {
        Settings2Screen screen = new Settings2Screen();
        screen.setCounter(view.getCounter());
        showScreen(screen);
    }

    @Override
    public void onClick(CounterListItemView view) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void refresh() {

    }

    public static interface SettingsListener{
        void onSettingsUpdated();
    }
}
