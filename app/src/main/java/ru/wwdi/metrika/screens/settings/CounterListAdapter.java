package ru.wwdi.metrika.screens.settings;

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

    private Settings1Screen mSettings1Screen;

    public CounterListAdapter(Settings1Screen settings1Screen, List<Counter> counters) {
        super(settings1Screen.getActivity(), R.layout.counter_list_item, counters);
        mSettings1Screen = settings1Screen;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CounterListItemView view;
        if (convertView == null || !(convertView instanceof CounterListItemView)) {
            view = new CounterListItemView(getContext());
            view.setListener(mSettings1Screen);
        } else {
            view = (CounterListItemView) convertView;
        }
        view.setCounter(getItem(position), true);
        return view;
    }

}
