package ru.wwdi.metrika.topPanel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.screens.counterList.CounterListAdapter;
import ru.wwdi.metrika.screens.counterList.CounterListItemView;
import ru.wwdi.metrika.webservice.Webservice;
import ru.wwdi.metrika.webservice.requests.BaseRequest;
import ru.wwdi.metrika.webservice.responses.CounterListResponse;

/**
 * Created by ryashentsev on 03.05.14.
 */
public class TopPanel extends SlideDownPanel {

    private ListView mCounterListContainer;
    private CounterListAdapter mAdapter;
    private View mCalendar;
    private View mConfirm;
    private CounterListItemView mCurrentCounterView;
    private TopPanelListener mListener;

    public TopPanel(Context context) {
        super(context);
        init();
    }

    public TopPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListener(TopPanelListener listener){
        mListener = listener;
    }

    public void updateCounters(){
        mAdapter.notifyDataSetChanged();
        initCurrentCounter();
        initCountersList();
    }

    private void init(){
        mCounterListContainer = (ListView) findViewById(R.id.counterList);
        mCounterListContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SharedPreferencesHelper.setSelectedCounter(mAdapter.getItem(position));
                mAdapter.notifyDataSetChanged();
                mListener.onSelectedCounterChanged();
                initCurrentCounter();
            }
        });

        mCalendar = findViewById(R.id.calendar);
        mCalendar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.onCalendarClick();
            }
        });
        mConfirm = findViewById(R.id.confirm);
        mConfirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        mCurrentCounterView = (CounterListItemView) findViewById(R.id.currentCounter);

        initCountersList();
        initCurrentCounter();
    }

    private void initCurrentCounter(){
        Counter selectedCounter = SharedPreferencesHelper.getSelectedCounter();
        mCurrentCounterView.setCounter(selectedCounter);
    }

    private void initCountersList(){
        Webservice.getCounterList(true, new BaseRequest.Listener<CounterListResponse>() {
            @Override
            public void onResponse(CounterListResponse response) {
                if(!response.hasErrors()) {
                    List<Counter> counters = response.getCounters();
                    List<Counter> visibleCounters = new ArrayList<Counter>();
                    Counter c;
                    for(int i=0;i<counters.size();i++){
                        c = counters.get(i);
                        if(c.isVisible()) visibleCounters.add(c);
                    }
                    mAdapter = new CounterListAdapter(getContext(), visibleCounters);
                    mCounterListContainer.setAdapter(mAdapter);


                    View v = mAdapter.getView(0, null, null);
                    v.measure(MeasureSpec.makeMeasureSpec(mCounterListContainer.getWidth(), MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(1, MeasureSpec.UNSPECIFIED));
                    int maxCounterListHeight = 4 * (v.getMeasuredHeight()+1);
                    ViewGroup.LayoutParams lp = mCounterListContainer.getLayoutParams();
                    int height = (v.getMeasuredHeight()+1)*visibleCounters.size();
                    if(height>maxCounterListHeight) height = maxCounterListHeight;
                    lp.height = height;
                    mCounterListContainer.setLayoutParams(lp);
                }
            }
        });
    }


    public static interface TopPanelListener{
        void onCalendarClick();
        void onSelectedCounterChanged();
    }

}
