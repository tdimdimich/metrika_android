package ru.wwdi.metrika.screens.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.CounterColorsHelper;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.screens.BaseScreen;
import ru.wwdi.metrika.screens.counterList.CounterListItemView;

/**
 * Created by ryashentsev on 04.05.14.
 */
public class Settings2Screen extends BaseScreen {

    private Counter mCounter;
    private int mSelectedColor;

    public void setCounter(Counter counter){
        mCounter = counter;
        mSelectedColor = mCounter.getColor();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings2, null);
        CounterListItemView currentCounterView = (CounterListItemView) v.findViewById(R.id.currentCounter);
        currentCounterView.setCounter(mCounter);

        GridView gridView = (GridView) v.findViewById(R.id.colorsGrid);
        final ColorsAdapter adapter = new ColorsAdapter(getActivity());
        adapter.setSelectedColor(mSelectedColor);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedColor = adapter.getItem(position);
                adapter.setSelectedColor(mSelectedColor);
            }
        });

        v.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        v.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCounter.setColor(mSelectedColor);
                mCounter.setGradientColor(CounterColorsHelper.getGradientColorForcolor(mSelectedColor));
                mCounter.save();
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    @Override
    public void refresh() {

    }
}
