package ru.wwdi.metrika.screens.totalSummary;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.models.OSStat;
import ru.wwdi.metrika.webservice.responses.OSStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class OSView extends FrameLayout {

    private View mFirstContainer;
    private View mFirstOsBar;
    private ImageView mFirstOsImage;
    private TextView mFirstOsName;
    private TextView mFirstOsValue;

    private View mSecondContainer;
    private ImageView mSecondOsImage;
    private TextView mSecondOsName;
    private TextView mSecondOsValue;

    private View mThirdContainer;
    private ImageView mThirdOsImage;
    private TextView mThirdOsName;
    private TextView mThirdOsValue;

    public OSView(Context context) {
        super(context);
        init();
    }

    public OSView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OSView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_oss, this);

        mFirstContainer = findViewById(R.id.firstContainer);
        mFirstOsBar = findViewById(R.id.firstOsBar);
        mFirstOsImage = (ImageView) findViewById(R.id.firstOsImage);
        mFirstOsName = (TextView) findViewById(R.id.firstOsName);
        mFirstOsValue = (TextView) findViewById(R.id.firstOsValue);

        mSecondContainer = findViewById(R.id.secondContainer);
        mSecondOsImage = (ImageView) findViewById(R.id.secondOsImage);
        mSecondOsName = (TextView) findViewById(R.id.secondOsName);
        mSecondOsValue = (TextView) findViewById(R.id.secondOsValue);

        mThirdContainer = findViewById(R.id.thirdContainer);
        mThirdOsImage = (ImageView) findViewById(R.id.thirdOsImage);
        mThirdOsName = (TextView) findViewById(R.id.thirdOsName);
        mThirdOsValue = (TextView) findViewById(R.id.thirdOsValue);
    }

    public void setData(Counter counter, OSStatResponse data){
        List<OSStat> list = data.getStats();

        int totalVisits=0;
        if(list.size()>0) totalVisits+=list.get(0).getVisits();
        if(list.size()>1) totalVisits+=list.get(1).getVisits();
        if(list.size()>2) totalVisits+=list.get(2).getVisits();


        if(list.size()>0){
            mFirstContainer.setVisibility(VISIBLE);

            mFirstOsBar.setBackgroundColor(counter.getColor());
            mFirstOsImage.setImageResource(getBrowserImageResourceByName(list.get(0).getName()));

            int highlightColor = counter.getColor();
            PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(highlightColor, PorterDuff.Mode.SRC_ATOP);
            mFirstOsImage.setColorFilter(colorFilter);

            mFirstOsName.setText(list.get(0).getName());
            mFirstOsValue.setText(String.format("(%.1f%%)", 1f * 100 * list.get(0).getVisits() / totalVisits));
        }else{
            mFirstContainer.setVisibility(GONE);
        }
        if(list.size()>1){
            mSecondContainer.setVisibility(VISIBLE);

            mSecondOsImage.setImageResource(getBrowserImageResourceByName(list.get(1).getName()));
            mSecondOsName.setText(list.get(1).getName());
            mSecondOsValue.setText(String.format("(%.1f%%)", 1f * 100 * list.get(1).getVisits() / totalVisits));
        }else{
            mSecondContainer.setVisibility(GONE);
        }
        if(list.size()>2){
            mThirdContainer.setVisibility(VISIBLE);

            mThirdOsImage.setImageResource(getBrowserImageResourceByName(list.get(2).getName()));
            mThirdOsName.setText(list.get(2).getName());
            mThirdOsValue.setText(String.format("(%.1f%%)", 1f * 100 * list.get(2).getVisits() / totalVisits));
        }else{
            mThirdContainer.setVisibility(GONE);
        }

    }

    private int getBrowserImageResourceByName(String name){
        if(name.toLowerCase().contains("ios") || name.toLowerCase().contains("mac")) return R.drawable.os_apple;
        if(name.toLowerCase().contains("gnu") || name.toLowerCase().contains("linux")) return R.drawable.os_linux;
        if(name.toLowerCase().contains("windows")) return R.drawable.os_windows;
        if(name.toLowerCase().contains("android")) return R.drawable.os_android;
        return R.drawable.os_unknown;
    }

}
