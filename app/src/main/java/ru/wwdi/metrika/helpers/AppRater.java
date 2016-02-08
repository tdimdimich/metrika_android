package ru.wwdi.metrika.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import ru.wwdi.metrika.R;

/**
 * Created by ryashentsev on 23.05.14.
 */
public class AppRater {

    private final static String APP_PNAME = "com.wwdi.metrika";

    private final static int LAUNCHES_UNTIL_PROMPT = 5;

    public static void increaseLaunchCount(){
        if (SharedPreferencesHelper.getDontShowRateDialog()) {
            return;
        }

        int launchCount = SharedPreferencesHelper.getLaunchCount();
        SharedPreferencesHelper.setLaunchCount(launchCount + 1);
    }

    public static void checkLaunchCount(final Activity acitivty){
        if (SharedPreferencesHelper.getLaunchCount() >= LAUNCHES_UNTIL_PROMPT) {
            SharedPreferencesHelper.setLaunchCount(0);
            showRateDialog(acitivty);
        }
    }

    public static void showRateDialog(final Activity activity) {

        View v = LayoutInflater.from(activity).inflate(R.layout.rater, null);

        final Dialog dialog = new Dialog(activity, R.style.full_screen_dialog);

        View b1 = v.findViewById(R.id.rate);
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                dialog.dismiss();
            }
        });

        View b2 = v.findViewById(R.id.remind);
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        View b3 = v.findViewById(R.id.noThanks);
        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferencesHelper.setDontShowRateDialog();
                dialog.dismiss();
            }
        });

        dialog.setContentView(v);
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }
}
