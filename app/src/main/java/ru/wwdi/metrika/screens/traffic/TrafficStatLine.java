package ru.wwdi.metrika.screens.traffic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;
import ru.wwdi.metrika.helpers.TimeFormatter;
import ru.wwdi.metrika.models.DateInterval;
import ru.wwdi.metrika.models.TrafficSummaryStat;
import ru.wwdi.metrika.util.CustomAnimator;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 13.05.14.
 */
public class TrafficStatLine extends FrameLayout {

    private TextView mTitle;
    private TextView mValue;
    private TextView mMinValue;
    private TextView mMinDate;
    private TextView mMaxValue;
    private TextView mMaxDate;
    private View mArrow;
    private View mPoints;

    private View mSublinesContainer;
    private TrafficStatSubline mSubline1;
    private TrafficStatSubline mSubline2;
    private TrafficStatSubline mSubline3;
    private TrafficStatSubline mSubline4;
    private TrafficStatSubline mSubline5;
    private TrafficStatSubline mSubline6;
    private TrafficStatSubline[] mSublines;
    private View mDivider;

    private CustomAnimator mAnimation;
    private boolean mIsOpened = false;
    private boolean mShowSublines = true;

    private List<List<TrafficSummaryStat>> mSublinesData;

    public TrafficStatLine(Context context) {
        super(context);
        init();
    }

    public TrafficStatLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TrafficStatLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void open(boolean fast){
        if(!mShowSublines) return;
        if(mAnimation!=null) return;
        mSublinesContainer.measure(MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.UNSPECIFIED));
        final int openedHeight = mSublinesContainer.getMeasuredHeight();
        if(fast){
            ViewGroup.LayoutParams lp = mSublinesContainer.getLayoutParams();
            lp.height = openedHeight;
            mSublinesContainer.setLayoutParams(lp);
            mArrow.setVisibility(VISIBLE);
            mPoints.setVisibility(INVISIBLE);
        }else{
            final int startHeight = mSublinesContainer.getHeight();
            mAnimation = new CustomAnimator(300, new CustomAnimator.CustomAnimatorListener() {
                @Override
                public void onDoStep(float partOf1) {
                    ViewGroup.LayoutParams lp = mSublinesContainer.getLayoutParams();
                    lp.height = (int) (startHeight+(openedHeight-startHeight)*partOf1);
                    mSublinesContainer.setLayoutParams(lp);
                    if(partOf1==1){
                        mAnimation = null;
                    }
                }
            });
            mAnimation.start();

            mArrow.setVisibility(VISIBLE);
            AlphaAnimation a = new AlphaAnimation(0, 1);
            a.setDuration(300);
            mArrow.startAnimation(a);
            a = new AlphaAnimation(1, 0);
            a.setDuration(300);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    mPoints.setVisibility(INVISIBLE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mPoints.startAnimation(a);
        }
        mIsOpened = true;
    }

    public void close(boolean fast){
        if(!mShowSublines) return;
        if(mAnimation!=null) return;
        if(fast){
            ViewGroup.LayoutParams lp = mSublinesContainer.getLayoutParams();
            lp.height = 0;
            mSublinesContainer.setLayoutParams(lp);
            mArrow.setVisibility(INVISIBLE);
            mPoints.setVisibility(VISIBLE);
        }else{
            final int startHeight = mSublinesContainer.getHeight();
            mAnimation = new CustomAnimator(300, new CustomAnimator.CustomAnimatorListener() {
                @Override
                public void onDoStep(float partOf1) {
                    ViewGroup.LayoutParams lp = mSublinesContainer.getLayoutParams();
                    lp.height = (int) (startHeight - startHeight*partOf1);
                    mSublinesContainer.setLayoutParams(lp);
                    if(partOf1==1){
                        mAnimation = null;
                    }
                }
            });
            mAnimation.start();
            AlphaAnimation a = new AlphaAnimation(1, 0);
            a.setDuration(300);
            a.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    mArrow.setVisibility(INVISIBLE);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mArrow.startAnimation(a);
            a = new AlphaAnimation(0, 1);
            a.setDuration(300);
            mPoints.setVisibility(VISIBLE);
            mPoints.startAnimation(a);
        }
        mIsOpened = false;
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.traffic_stat_line, this);
        mTitle = (TextView) findViewById(R.id.title);
        mValue = (TextView) findViewById(R.id.value);
        mMinValue = (TextView) findViewById(R.id.minValue);
        mMinDate = (TextView) findViewById(R.id.minDate);
        mMaxValue = (TextView) findViewById(R.id.maxValue);
        mMaxDate = (TextView) findViewById(R.id.maxDate);
        mArrow = findViewById(R.id.arrow);
        mPoints = findViewById(R.id.points);
        mSublinesContainer = findViewById(R.id.sublinesContainer);

        mSubline1 = (TrafficStatSubline) findViewById(R.id.subline1);
        mSubline2 = (TrafficStatSubline) findViewById(R.id.subline2);
        mSubline3 = (TrafficStatSubline) findViewById(R.id.subline3);
        mSubline4 = (TrafficStatSubline) findViewById(R.id.subline4);
        mSubline5 = (TrafficStatSubline) findViewById(R.id.subline5);
        mSubline6 = (TrafficStatSubline) findViewById(R.id.subline6);
        mSublines = new TrafficStatSubline[]{mSubline1, mSubline2, mSubline3, mSubline4, mSubline5, mSubline6};
        mDivider = findViewById(R.id.lineDivider);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mShowSublines) return;
                if(mIsOpened){
                    close(false);
                }else{
                    open(false);
                }
            }
        });
    }

    public void setData(int color, TrafficSummaryStatResponse data, boolean[] selectedPoints, TrafficScreen.DataType dataType){
        if(selectedPoints==null) selectedPoints = new boolean[]{true,true,true,true,true,true};
        if(selectedPoints.length!=6) throw new IllegalArgumentException("SelectedPoints must be always of size 6!");
        initViews(data, selectedPoints, dataType);
        setColor(color);
    }

    private void setColor(int color){
        for(TrafficStatSubline subline: mSublines){
            subline.setColor(color);
        }
        mValue.setTextColor(color);
        mDivider.setBackgroundColor(color);
    }

    private DateInterval getDateIntervalOfSublineData(List<TrafficSummaryStat> sublineData){
        if(sublineData.size()==0) return null;
        Date startDate=null;
        Date endDate=null;
        for(TrafficSummaryStat stat: sublineData){
            if(endDate==null || endDate.getTime()<stat.getDate().getTime()){
                endDate = stat.getDate();
            }
            if(startDate==null || startDate.getTime()>stat.getDate().getTime()){
                startDate = stat.getDate();
            }
        }
        return new DateInterval(startDate, endDate);
    }

    private void initViews(TrafficSummaryStatResponse data, boolean[] selectedPoints, TrafficScreen.DataType dataType){
        DateInterval interval = SharedPreferencesHelper.getCurrentDateInterval();
        mShowSublines = interval.getEndDate().getTime()!=interval.getStartDate().getTime();

        float value = getValueFromSingleStatObject(data.getTotals(), dataType);

        if(mShowSublines){
            mSublinesContainer.setVisibility(VISIBLE);
            //формируем все необходимые данные типа value строк и подстроки, а так же прочие данные
            float[] sublineValues = new float[6];
            DateInterval[] sublineDateIntervals = new DateInterval[6];
            List<List<TrafficSummaryStat>> sublinesData = new ArrayList<List<TrafficSummaryStat>>();
            for(int i=0;i<6;i++){
                sublinesData.add(getDataForPart(data, i));
                if(selectedPoints[i]){
                    sublineValues[i] = getSublineValue(sublinesData.get(i), dataType);
                    sublineDateIntervals[i] = getDateIntervalOfSublineData(sublinesData.get(i));
                }
            }

            //настраиваем вьюшки
            for(int i=0;i<6;i++){
                if(selectedPoints[i]){
                    if(sublineDateIntervals[i]==null){
                        mSublines[i].setVisibility(GONE);
                    }else{
                        mSublines[i].setVisibility(VISIBLE);
                        mSublines[i].setData(sublineDateIntervals[i],
                                getValueString(sublineValues[i], dataType),
                                getSublinePerc(sublineValues, value, i, dataType));
                    }
                }else{
                    mSublines[i].setVisibility(GONE);
                }
            }
        }else{
            mPoints.setVisibility(GONE);
            mArrow.setVisibility(GONE);
            mSublinesContainer.setVisibility(GONE);
        }

        mValue.setText(getValueString(value, dataType));

        mMaxValue.setText(getValueString(getValueFromSingleStatObject(data.getMax(), dataType), dataType).replace(",", ""));
        mMinValue.setText(getValueString(getValueFromSingleStatObject(data.getMin(), dataType), dataType).replace(",", ""));

        mTitle.setText((getTitleByDataType(dataType)));
        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        mMinDate.setText(format.format(getMinDate(data, dataType)));
        mMaxDate.setText(format.format(getMaxDate(data, dataType)));
    }

    private Date getMinDate(TrafficSummaryStatResponse data, TrafficScreen.DataType dataType){
        Date minDate = null;
        float minValue = -1;
        float value;
        for(TrafficSummaryStat stat: data.getTrafficSummaries()){
            value = getValueFromSingleStatObject(stat, dataType);
            if(minValue==-1 || minValue>value){
                minValue = value;
                minDate = stat.getDate();
            }
        }
        return minDate;
    }

    private Date getMaxDate(TrafficSummaryStatResponse data, TrafficScreen.DataType dataType){
        Date maxDate = null;
        float maxValue = -1;
        float value;
        for(TrafficSummaryStat stat: data.getTrafficSummaries()){
            value = getValueFromSingleStatObject(stat, dataType);
            if(maxValue==-1 || maxValue<value){
                maxValue = value;
                maxDate = stat.getDate();
            }
        }
        return maxDate;
    }

    private String getTitleByDataType(TrafficScreen.DataType dataType){
        int titleResource = 0;
        if(dataType== TrafficScreen.DataType.VISITS){
            titleResource = R.string.traffic_visits;
        }else if(dataType== TrafficScreen.DataType.VISITORS){
            titleResource = R.string.traffic_visitors;
        }else if(dataType== TrafficScreen.DataType.NEW_VISITORS){
            titleResource = R.string.traffic_new;
        }else if(dataType== TrafficScreen.DataType.VIEWS){
            titleResource = R.string.traffic_views;
        }else if(dataType== TrafficScreen.DataType.DENIAL){
            titleResource = R.string.traffic_denies;
        }else if(dataType== TrafficScreen.DataType.VIEW_DEPTH){
            titleResource = R.string.traffic_depth;
        }else if(dataType== TrafficScreen.DataType.VISIT_TIME){
            titleResource = R.string.traffic_time;
        }
        return getResources().getString(titleResource);
    }

    private float getSublinePerc(float[] sublineValues, float totalValue, int position, TrafficScreen.DataType dataType){
        if(dataType== TrafficScreen.DataType.DENIAL ||
                dataType== TrafficScreen.DataType.VIEW_DEPTH ||
                dataType== TrafficScreen.DataType.VISIT_TIME){
            float max = sublineValues[0];
            for(int i=1;i<6;i++){
                max = Math.max(max, sublineValues[i]);
            }
            if(max==0) return 0;
            return 100*sublineValues[position]/max;
        }else{
            if(totalValue==0) return 0;
            return 100*sublineValues[position]/totalValue;
        }
    }

    private String getValueString(float value, TrafficScreen.DataType dataType){
        if(dataType== TrafficScreen.DataType.DENIAL){
            return String.format("%d%%", (int) value);
        }else if(dataType== TrafficScreen.DataType.VIEW_DEPTH){
            return String.format("%.2f", value);
        }else if(dataType== TrafficScreen.DataType.VISIT_TIME){
            return TimeFormatter.formatTimeWithSeconds((int)value);
        }else{
            return String.valueOf((int)value);
        }
    }

    private float getValueFromSingleStatObject(TrafficSummaryStat data, TrafficScreen.DataType dataType){
        if(dataType== TrafficScreen.DataType.VISITS){
            return data.getVisits();
        }else if(dataType== TrafficScreen.DataType.VISITORS){
            return data.getVisitors();
        }else if(dataType== TrafficScreen.DataType.NEW_VISITORS){
            return data.getNewVisitors();
        }else if(dataType== TrafficScreen.DataType.VIEWS){
            return data.getPageViews();
        }else if(dataType== TrafficScreen.DataType.DENIAL){
            return data.getDenial()*100;
        }else if(dataType== TrafficScreen.DataType.VIEW_DEPTH){
            return data.getDepth();
        }else if(dataType== TrafficScreen.DataType.VISIT_TIME){
            return data.getVisitTime();
        }
        return 0;
    }

    private float getSublineValue(List<TrafficSummaryStat> sublineData, TrafficScreen.DataType dataType){
        float result = 0;
        if(sublineData.size()==0) return 0;
        for(int i=0;i<sublineData.size();i++){
            result+=getValueFromSingleStatObject(sublineData.get(i), dataType);
        }
        if(dataType== TrafficScreen.DataType.DENIAL ||
                dataType== TrafficScreen.DataType.VIEW_DEPTH){
            result /= sublineData.size();
        }else if(dataType== TrafficScreen.DataType.VISIT_TIME){
            result = (int) (result/sublineData.size());
        }
        return result;
    }

    private int getPartSize(int totalSize, int position){
        if(position>=totalSize) return 0;
        if(totalSize<=6) return 1;
        int partSize = totalSize/6;
        int delta = totalSize - partSize*6;
        if(delta>position) partSize++;
        return partSize;
    }

    private List<TrafficSummaryStat> getDataForPart(TrafficSummaryStatResponse totalData, int position){
        int size = getPartSize(totalData.getTrafficSummaries().size(), position);
        if(size==0) return new ArrayList<TrafficSummaryStat>();
        int startPosition=0;
        for(int i=0;i<position;i++){
            startPosition += getPartSize(totalData.getTrafficSummaries().size(), i);
        }
        ArrayList<TrafficSummaryStat> result = new ArrayList<TrafficSummaryStat>();
        for(int i=startPosition;i<startPosition+size;i++){
            result.add(totalData.getTrafficSummaries().get(i));
        }
        return result;
    }

}
