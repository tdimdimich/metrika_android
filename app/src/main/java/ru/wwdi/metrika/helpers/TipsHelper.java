package ru.wwdi.metrika.helpers;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;

/**
 * Created by ryashentsev on 29.05.14.
 */
public class TipsHelper {

    public static void showSwipeTipIfNeeded(Activity activity){
        if(SharedPreferencesHelper.isSwipeTipShown()) return;
        SharedPreferencesHelper.setSwipeTipShown();
        showTip(activity, R.string.swipe_tip, R.drawable.fingerprint);
    }

    public static void showRotateTipIfNeeded(Activity activity){
        if(SharedPreferencesHelper.isRotateTipShown()) return;
        SharedPreferencesHelper.setRotateTipShown();
        showTip(activity, R.string.rotate_tip, R.drawable.rotate);
    }

    private static void showTip(Activity activity, int textResource, int imageResource){
        try{
            final Dialog dialog = new Dialog(activity, R.style.full_screen_dialog);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.tip);
            ImageView tipImage = (ImageView) dialog.findViewById(R.id.tipImage);
            tipImage.setImageResource(imageResource);
            TextView tipText = (TextView) dialog.findViewById(R.id.tipText);
            tipText.setText(textResource);
            View root = dialog.findViewById(R.id.tipRoot);
            root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        }catch (Exception e){
            Log.e(YandexMetrikaApplication.class.getSimpleName(), "Can't show tip", e);
        }
    }

}
