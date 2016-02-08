package ru.wwdi.metrika.screens.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import ru.wwdi.metrika.helpers.CounterColorsHelper;
import ru.wwdi.metrika.helpers.PxDpHelper;
import ru.wwdi.metrika.views.CircleViewWithGrayBg;

/**
 * Created by ryashentsev on 04.05.14.
 */
public class ColorsAdapter extends ArrayAdapter<Integer> {

    private int mSelectedColor;

    public ColorsAdapter(Context context) {
        super(context, 0, CounterColorsHelper.getColors());
    }

    public void setSelectedColor(int color){
        mSelectedColor = color;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CircleViewWithGrayBg view;
        if (convertView == null || !(convertView instanceof CircleViewWithGrayBg)) {
            view = new CircleViewWithGrayBg(getContext());
            int size = PxDpHelper.dpToPx(35);
            ViewGroup.LayoutParams params = new GridView.LayoutParams(size, size);
            view.setLayoutParams(params);
        } else {
            view = (CircleViewWithGrayBg) convertView;
        }
        view.setGrayBgVisible(mSelectedColor==getItem(position).intValue());
        view.update(getItem(position), true);
        return view;
    }
}
