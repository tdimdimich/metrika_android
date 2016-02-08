package ru.wwdi.metrika.screens.settings;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.views.CircleView;

/**
 * Created by dmitrykorotchenkov on 14/04/14.
 */
public class CounterListItemView extends FrameLayout {

    private CircleView mCircle;
    private TextView mTitle;
    private ImageButton mVisible;
    private ImageButton mSettings;
    private Counter mCounter;
    private CounterListItemViewListener mListener;

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

    public void setListener(CounterListItemViewListener listener){
        mListener = listener;
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.counter_list_item_for_settings, this);
        mCircle = (CircleView) findViewById(R.id.circleView);
        mCircle.update(Color.MAGENTA, true);
        mTitle = (TextView) findViewById(R.id.counterTitle);
        mVisible = (ImageButton) findViewById(R.id.visible);
        mVisible.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.onVisibilityClick(CounterListItemView.this);
            }
        });
        mSettings = (ImageButton) findViewById(R.id.settings);
        mSettings.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.onSettingsClick(CounterListItemView.this);
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null) mListener.onClick(CounterListItemView.this);
            }
        });
    }

    public void setCounter(Counter counter, boolean selected){
        mCounter = counter;
        mTitle.setText(counter.getName());
        mTitle.setTextColor(counter.getColor());
        mVisible.setImageResource(mCounter.isVisible()?R.drawable.visible_icon:R.drawable.unvisible_icon);
        mCircle.update(mCounter.getColor(), selected);
    }

    public Counter getCounter(){
        return mCounter;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCircle.update(mCounter.getColor(), true);
    }

    public static interface CounterListItemViewListener{
        void onVisibilityClick(CounterListItemView view);
        void onSettingsClick(CounterListItemView view);
        void onClick(CounterListItemView view);
    }

}
