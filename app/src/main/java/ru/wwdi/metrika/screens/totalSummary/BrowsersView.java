package ru.wwdi.metrika.screens.totalSummary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.models.BrowserStat;
import ru.wwdi.metrika.models.Counter;
import ru.wwdi.metrika.webservice.responses.BrowsersStatResponse;

/**
 * Created by ryashentsev on 08.05.14.
 */
public class BrowsersView extends FrameLayout {

    private View mFirstBrowserBarContainer;
    private View mFirstBrowserBar;
    private ImageView mFirstBrowserImage;
    private TextView mFirstBrowserName;
    private TextView mFirstBrowserValue;

    private View mSecondBrowserBarContainer;
    private View mSecondBrowserBar;
    private ImageView mSecondBrowserImage;
    private TextView mSecondBrowserName;
    private TextView mSecondBrowserValue;

    private View mThirdBrowserBarContainer;
    private View mThirdBrowserBar;
    private ImageView mThirdBrowserImage;
    private TextView mThirdBrowserName;
    private TextView mThirdBrowserValue;

    public BrowsersView(Context context) {
        super(context);
        init();
    }

    public BrowsersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BrowsersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.total_summary_browsers, this);

        mFirstBrowserBarContainer = findViewById(R.id.firstBrowserBarContainer);
        mFirstBrowserBar = findViewById(R.id.firstBrowserBar);
        mFirstBrowserImage = (ImageView) findViewById(R.id.firstBrowserImage);
        mFirstBrowserName = (TextView) findViewById(R.id.firstBrowserName);
        mFirstBrowserValue = (TextView) findViewById(R.id.firstBrowserValue);

        mSecondBrowserBarContainer = findViewById(R.id.secondBrowserBarContainer);
        mSecondBrowserBar = findViewById(R.id.secondBrowserBar);
        mSecondBrowserImage = (ImageView) findViewById(R.id.secondBrowserImage);
        mSecondBrowserName = (TextView) findViewById(R.id.secondBrowserName);
        mSecondBrowserValue = (TextView) findViewById(R.id.secondBrowserValue);

        mThirdBrowserBarContainer = findViewById(R.id.thirdBrowserBarContainer);
        mThirdBrowserBar = findViewById(R.id.thirdBrowserBar);
        mThirdBrowserImage = (ImageView) findViewById(R.id.thirdBrowserImage);
        mThirdBrowserName = (TextView) findViewById(R.id.thirdBrowserName);
        mThirdBrowserValue = (TextView) findViewById(R.id.thirdBrowserValue);
    }

    public void setData(Counter counter, BrowsersStatResponse data){
        List<BrowserStat> list = data.getStats();

        int totalVisits=0;
        if(list.size()>0) totalVisits+=list.get(0).getVisits();
        if(list.size()>1) totalVisits+=list.get(1).getVisits();
        if(list.size()>2) totalVisits+=list.get(2).getVisits();


        if(list.size()>0){
            mFirstBrowserBarContainer.setVisibility(VISIBLE);
            mFirstBrowserBar.setVisibility(VISIBLE);
            mFirstBrowserImage.setVisibility(VISIBLE);
            mFirstBrowserName.setVisibility(VISIBLE);
            mFirstBrowserValue.setVisibility(VISIBLE);

            mFirstBrowserBar.setBackgroundColor(counter.getColor());
            ViewGroup.LayoutParams lp = mFirstBrowserBar.getLayoutParams();
            lp.width = (int) (((View)mFirstBrowserBar.getParent()).getLayoutParams().width*1f*list.get(0).getVisits()/totalVisits);
            mFirstBrowserBar.setLayoutParams(lp);
            mFirstBrowserImage.setImageResource(getBrowserImageResourceByName(list.get(0).getName()));
            mFirstBrowserName.setText(list.get(0).getName());
            mFirstBrowserName.setTextColor(counter.getColor());
            mFirstBrowserValue.setText(String.format("(%.1f%%)", 1f*100*list.get(0).getVisits()/totalVisits));
            mFirstBrowserValue.setTextColor(counter.getColor());
        }else{
            mFirstBrowserBarContainer.setVisibility(GONE);
            mFirstBrowserBar.setVisibility(GONE);
            mFirstBrowserImage.setVisibility(GONE);
            mFirstBrowserName.setVisibility(GONE);
            mFirstBrowserValue.setVisibility(GONE);
        }
        if(list.size()>1){
            mSecondBrowserBarContainer.setVisibility(VISIBLE);
            mSecondBrowserBar.setVisibility(VISIBLE);
            mSecondBrowserImage.setVisibility(VISIBLE);
            mSecondBrowserName.setVisibility(VISIBLE);
            mSecondBrowserValue.setVisibility(VISIBLE);

            ViewGroup.LayoutParams lp = mSecondBrowserBar.getLayoutParams();
            lp.width = (int) (((View)mSecondBrowserBar.getParent()).getLayoutParams().width*1f*list.get(1).getVisits()/totalVisits);
            mSecondBrowserBar.setLayoutParams(lp);
            mSecondBrowserImage.setImageResource(getBrowserImageResourceByName(list.get(1).getName()));
            mSecondBrowserName.setText(list.get(1).getName());
            mSecondBrowserValue.setText(String.format("(%.1f%%)", 1f*100*list.get(1).getVisits()/totalVisits));
        }else{
            mSecondBrowserBarContainer.setVisibility(GONE);
            mSecondBrowserBar.setVisibility(GONE);
            mSecondBrowserImage.setVisibility(GONE);
            mSecondBrowserName.setVisibility(GONE);
            mSecondBrowserValue.setVisibility(GONE);
        }
        if(list.size()>2){
            mThirdBrowserBarContainer.setVisibility(VISIBLE);
            mThirdBrowserBar.setVisibility(VISIBLE);
            mThirdBrowserImage.setVisibility(VISIBLE);
            mThirdBrowserName.setVisibility(VISIBLE);
            mThirdBrowserValue.setVisibility(VISIBLE);

            ViewGroup.LayoutParams lp = mThirdBrowserBar.getLayoutParams();
            lp.width = (int) (((View)mThirdBrowserBar.getParent()).getLayoutParams().width*1f*list.get(2).getVisits()/totalVisits);
            mThirdBrowserBar.setLayoutParams(lp);
            mThirdBrowserImage.setImageResource(getBrowserImageResourceByName(list.get(2).getName()));
            mThirdBrowserName.setText(list.get(2).getName());
            mThirdBrowserValue.setText(String.format("(%.1f%%)", 1f*100*list.get(2).getVisits()/totalVisits));
        }else{
            mThirdBrowserBarContainer.setVisibility(GONE);
            mThirdBrowserBar.setVisibility(GONE);
            mThirdBrowserImage.setVisibility(GONE);
            mThirdBrowserName.setVisibility(GONE);
            mThirdBrowserValue.setVisibility(GONE);
        }

    }

    private int getBrowserImageResourceByName(String name){
        if(name.toLowerCase().contains("firefox")) return R.drawable.browser_icon_firefox;
        if(name.toLowerCase().contains("opera")) return R.drawable.browser_icon_opera;
        if(name.toLowerCase().contains("google chrome")) return R.drawable.browser_icon_chrome;
        if(name.toLowerCase().contains("msie")) return R.drawable.browser_icon_ie;
        if(name.toLowerCase().contains("safari")) return R.drawable.browser_icon_safari;
        return R.drawable.browser_icon_unknown;
    }

}
