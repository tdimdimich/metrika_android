package ru.wwdi.metrika.screens.counterList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.views.CircleView;

/**
 * Created by dmitrykorotchenkov on 14/04/14.
 */
public class CounterListItemView extends FrameLayout {

    private CircleView mCircle;
    private TextView mTitle;

    public CounterListItemView(Context context) {
        super(context);
        init();
    }

    public CounterListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CounterListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.counter_list_item, this);
        mCircle = (CircleView) findViewById(R.id.circleView);
        mTitle = (TextView) findViewById(R.id.counterTitle);
    }

    public void setCounter(Counter counter){
        mTitle.setText(counter.getName());
        mTitle.setTextColor(counter.getColor());
        Counter selectedCounter = SharedPreferencesHelper.getSelectedCounter();
        Long selectedCounterId = selectedCounter==null?null:selectedCounter.getCounterId();
        mCircle.update(counter.getColor(), counter.getCounterId().equals(selectedCounterId));
    }

}
