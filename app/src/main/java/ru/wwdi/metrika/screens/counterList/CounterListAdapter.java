package ru.wwdi.metrika.screens.counterList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.Counter;

/**
 * Created by dmitrykorotchenkov on 15/04/14.
 */
public class CounterListAdapter extends ArrayAdapter<Counter> {

    private List<Counter> mVisibleCounters;

    public CounterListAdapter(Context context, List<Counter> visibleCounters) {
        super(context, R.layout.counter_list_item, visibleCounters);
        mVisibleCounters = visibleCounters;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CounterListItemView view;
        if (convertView == null || !(convertView instanceof CounterListItemView)) {
            view = new CounterListItemView(getContext());
        } else {
            view = (CounterListItemView) convertView;
        }
        view.setCounter(mVisibleCounters.get(position));
        return view;
    }

}
