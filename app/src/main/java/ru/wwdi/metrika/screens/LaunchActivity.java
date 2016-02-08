package ru.wwdi.metrika.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ru.wwdi.metrika.helpers.AppRater;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.screens.counterList.CounterListActivity;

/**
 * Created by dmitrykorotchenkov on 21/04/14.
 */
public class LaunchActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<? extends Activity> activityClass;
        if (SharedPreferencesHelper.getToken()==null) {
            activityClass = WelcomeActivity.class;
        } else if(SharedPreferencesHelper.getSelectedCounter()==null){
            activityClass = CounterListActivity.class;
        } else {
            activityClass = MainScreen.class;
        }
        final Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        finish();
        AppRater.increaseLaunchCount();
    }
}
