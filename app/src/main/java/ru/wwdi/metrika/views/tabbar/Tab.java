package ru.wwdi.metrika.views.tabbar;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import ru.wwdi.metrika.R;

/**
 * Created with IntelliJ IDEA.
 * User: ryashentsev
 * Date: 20.11.13
 * Time: 11:31
 * To change this template use File | Settings | File Templates.
 */
public class Tab extends TextView{

    private boolean mAllowed = true;

    public Tab(Context context) {
        super(context);
        init();
    }

    public Tab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Tab(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        setGravity(Gravity.CENTER_HORIZONTAL);
        updatedTextAppearance();
    }

    public void setAllowed(boolean allowed) {
        mAllowed = allowed;
        updatedTextAppearance();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        updatedTextAppearance();
    }

    private void updatedTextAppearance(){
        if(!mAllowed){
            setTypeface(Typeface.DEFAULT);
            setTextColor(getResources().getColor(R.color.date_interval_tab_disabled_color));
        }else{
            if(isSelected()){
                setTypeface(Typeface.DEFAULT_BOLD);
            }else{
                setTypeface(Typeface.DEFAULT);
            }
            setTextColor(getResources().getColor(R.color.date_interval_tab_color));
        }
    }

}
