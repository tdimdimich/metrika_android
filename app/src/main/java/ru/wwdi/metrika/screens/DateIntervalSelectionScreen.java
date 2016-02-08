package ru.wwdi.metrika.screens;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.DateHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.screens.counterList.CounterListItemView;
import ru.wwdi.metrika.views.tabbar.Tab;
import ru.wwdi.metrika.views.tabbar.TabBar;

/**
 * Created by ryashentsev on 03.05.14.
 */
public class DateIntervalSelectionScreen extends Activity {

    private CounterListItemView mCurrentCounter;
    private TabBar mTabBar;
    private TextView mStartDate;
    private TextView mEndDate;

    private DateInterval mNewDateInterval = SharedPreferencesHelper.getCurrentDateInterval();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_interval_selection);
        mStartDate = (TextView) findViewById(R.id.startDate);
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartDate.setSelected(true);
                mEndDate.setSelected(false);
                getDateForView(mStartDate);
            }
        });
        mEndDate = (TextView) findViewById(R.id.endDate);
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEndDate.setSelected(true);
                mStartDate.setSelected(false);
                getDateForView(mEndDate);
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCurrentCounter = (CounterListItemView) findViewById(R.id.currentCounter);
        mCurrentCounter.setCounter(SharedPreferencesHelper.getSelectedCounter());
        mTabBar = (TabBar) findViewById(R.id.tabBar);
        initTabBar();
        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        updateTimeIntervalTexts();
    }

    private void getDateForView(final TextView textView){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(textView==mStartDate?mNewDateInterval.getStartDate():mNewDateInterval.getEndDate());
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mTabBar.setAllowed(false);
                Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                Date newStartDate = textView==mStartDate?calendar.getTime():mNewDateInterval.getStartDate();
                Date newEndDate = textView==mEndDate?calendar.getTime():mNewDateInterval.getEndDate();
                Date today = DateHelper.getTodayDate();
                if(newEndDate.getTime()>today.getTime()) newEndDate = today;
                if(newStartDate.getTime()>today.getTime()) newStartDate = today;
                if(textView==mStartDate){
                    if(newStartDate.getTime()>newEndDate.getTime()) newStartDate=newEndDate;
                }else if(textView==mEndDate){
                    if(newEndDate.getTime()<newStartDate.getTime()) newEndDate=newStartDate;
                }
                mNewDateInterval = new DateInterval(newStartDate, newEndDate);
                updateTimeIntervalTexts();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void confirm(){
        DateInterval oldDateInterval = SharedPreferencesHelper.getCurrentDateInterval();
        if(!oldDateInterval.equals(mNewDateInterval)){
            SharedPreferencesHelper.setCurrentDateInterval(mNewDateInterval);
            setResult(RESULT_OK);
        }else{
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void initTabBar(){
        Tab t;
        int[] tabNames = new int[]{R.string.today, R.string.week, R.string.month, R.string.year};
        ArrayList<Tab> tabs = new ArrayList<Tab>();
        for(int tabName: tabNames){
            t = new Tab(this);
            t.setText(tabName);
            tabs.add(t);
        }
        mTabBar.setTabs(tabs);
        mTabBar.setTabSelectListener(new TabBar.TabSelectListener() {
            @Override
            public void onTabSelected(Tab tab, int index) {
                mEndDate.setSelected(false);
                mStartDate.setSelected(false);
                mTabBar.setAllowed(true);
                updateTimeIntervals(index);
            }
        });
        DateInterval dateInterval = SharedPreferencesHelper.getCurrentDateInterval();
        if(DateHelper.getDateInterval(DateHelper.TODAY).equals(dateInterval)){
            mTabBar.selectTab(0);
        }else if(DateHelper.getDateInterval(DateHelper.WEEK).equals(dateInterval)){
            mTabBar.selectTab(1);
        }else if(DateHelper.getDateInterval(DateHelper.MONTH).equals(dateInterval)){
            mTabBar.selectTab(2);
        }else if(DateHelper.getDateInterval(DateHelper.YEAR).equals(dateInterval)){
            mTabBar.selectTab(3);
        }else{
            mTabBar.setAllowed(false);
        }
    }

    private void updateTimeIntervalTexts(){
        mStartDate.setText(DateHelper.formatDate(mNewDateInterval.getStartDate()));
        mEndDate.setText(DateHelper.formatDate(mNewDateInterval.getEndDate()));
    }

    private void updateTimeIntervals(int index){
        switch (index){
            case 0://today
                mNewDateInterval = DateHelper.getDateInterval(DateHelper.TODAY);
                break;
            case 1://week
                mNewDateInterval = DateHelper.getDateInterval(DateHelper.WEEK);
                break;
            case 2://month
                mNewDateInterval = DateHelper.getDateInterval(DateHelper.MONTH);
                break;
            case 3://year
                mNewDateInterval = DateHelper.getDateInterval(DateHelper.YEAR);
                break;
        }
        updateTimeIntervalTexts();
    }
}
