package ru.wwdi.metrika.screens;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.PxDpHelper;

/**
 * Created by ryashentsev on 05.05.14.
 */
public class CreatorsScreen extends BaseScreen {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.creators, null);

        View siteLink = v.findViewById(R.id.siteLink);
        siteLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.progress-engine.ru?utm_source=progress-engine&utm_campaign=MM_android&utm_medium=crc"));
                startActivity(intent);
            }
        });

        View emailLink = v.findViewById(R.id.emailLink);
        emailLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@progress-engine.ru"});
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), R.string.no_email_clients, Toast.LENGTH_SHORT).show();
                }
            }
        });

        v.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + "\n" + getString(R.string.share_url));
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_text));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                try{
                    startActivity(Intent.createChooser(intent, "Share URL"));
                }catch (ActivityNotFoundException e){
                    Toast.makeText(getActivity(), R.string.no_application_for_sharing, Toast.LENGTH_LONG);
                }
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updatePhotoSize(getResources().getConfiguration().screenWidthDp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updatePhotoSize(newConfig.screenWidthDp);
    }

    private void updatePhotoSize(int screenWidthDp){
        View v = getView();
        ImageView photo = (ImageView) v.findViewById(R.id.photo);
        BitmapDrawable bmd = (BitmapDrawable) photo.getDrawable();
        Bitmap b = bmd.getBitmap();
        float scale = 1f*b.getWidth()/b.getHeight();
        ViewGroup.LayoutParams lp = photo.getLayoutParams();
        lp.height = (int) (PxDpHelper.dpToPx(screenWidthDp)/scale);
        photo.setLayoutParams(lp);
    }

    @Override
    public void refresh() {

    }
}
