package ru.wwdi.metrika.menu;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.wwdi.metrika.R;

/**
 * Created by ryashentsev on 28.04.14.
 */
public class MenuItemView extends LinearLayout {

    private TextView mName;
    protected MenuItem mMenuItem;
    protected MenuItemClickListener mListener;

    public MenuItemView(Context context) {
        super(context);
    }

    public MenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setMenuItemClickListener(MenuItemClickListener listener){
        mListener = listener;
        mName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(MenuItemView.this);
            }
        });
    }

    @Override
    public void setSelected(boolean selected) {
        mName.setTypeface(selected ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
        super.setSelected(selected);
    }

    @Override
    protected void onFinishInflate() {
        mName = (TextView) findViewById(R.id.name);
        super.onFinishInflate();
    }

    public MenuItem getMenuItem(){
        return mMenuItem;
    }

    public void setMenuItem(MenuItem item){
        mMenuItem = item;
        mName.setText( getStringResourceByName(item.getName()) );
    }

    private String getStringResourceByName(String aString) {
        String packageName = getContext().getPackageName();
        int resId = getResources().getIdentifier(aString, "string", packageName);
        return getResources().getString(resId);
    }
      
    public static interface MenuItemClickListener{
        public void onClick(MenuItemView view);
    }

}
