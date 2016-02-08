package ru.wwdi.metrika.screens.traffic;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.Counter;

/**
 * Created by ryashentsev on 14.05.14.
 */
public class SpinnerMainView extends RelativeLayout {

    public SpinnerMainView(Context context) {
        super(context);
    }

    public SpinnerMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerMainView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Counter c = SharedPreferencesHelper.getSelectedCounter();
        findViewById(R.id.line1).setBackgroundColor(c.getColor());
        findViewById(R.id.line2).setBackgroundColor(c.getColor());
    }
}
