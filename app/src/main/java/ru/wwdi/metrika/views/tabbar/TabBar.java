package ru.wwdi.metrika.views.tabbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ryashentsev
 * Date: 20.11.13
 * Time: 11:30
 * To change this template use File | Settings | File Templates.
 */
public class TabBar extends LinearLayout implements View.OnClickListener {

    public static interface TabSelectListener{
        void onTabSelected(Tab tab, int index);
    }

    private List<Tab> mTabs;
    private Tab mSelectedTab;
    private TabSelectListener mTabSelectListener;
    private boolean mAllowed = true;

    public TabBar(Context context) {
        super(context);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTabSelectListener(TabSelectListener listener){
        mTabSelectListener = listener;
    }

    public void setTabs(List<Tab> tabs){
        if(tabs==null || tabs.size()==0) throw new IllegalArgumentException("tabs must not be null or empty!");
        removeAllViews();
        if(mTabs!=null){
            for(Tab t: tabs){
                t.setOnClickListener(null);
            }
        }
        mTabs = tabs;
        for(View t: tabs){
            addView(t);
            LayoutParams params = (LayoutParams)t.getLayoutParams();
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.weight = 1;
            t.setOnClickListener(this);
        }
    }

    public void setAllowed(boolean allowed) {
        if(mAllowed==allowed) return;
        mAllowed = allowed;
        for(Tab t: mTabs){
            t.setAllowed(allowed);
            t.setSelected(false);
            mSelectedTab = null;
        }
    }

    public void selectTab(Integer index){
        setAllowed(true);
        Integer selectedTabIndex = getSelectedTabIndex();
        if(index==null || !index.equals(selectedTabIndex)){
            if(mSelectedTab!=null){
                mSelectedTab.setSelected(false);
                mSelectedTab = null;
            }
        }
        if(index!=null && !index.equals(selectedTabIndex)){
            mSelectedTab = mTabs.get(index);
            mSelectedTab.setSelected(true);
            if(mTabSelectListener!=null) mTabSelectListener.onTabSelected(mSelectedTab, index);
        }
    }

    public Integer getSelectedTabIndex(){
        if(mSelectedTab==null) return null;
        return mTabs.indexOf(mSelectedTab);
    }

    public View getSelectedTab(){
        return mSelectedTab;
    }

    public Tab getTabAtIndex(int index){
        return mTabs.get(index);
    }

    @Override
    public void onClick(View v) {
        selectTab(mTabs.indexOf(v));
    }
}
