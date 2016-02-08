package ru.wwdi.metrika.screens.traffic.chart;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.YandexMetrikaApplication;
import ru.wwdi.metrika.helpers.PxDpHelper;
import ru.wwdi.metrika.models.DateInterval;

/**
 * Created by ryashentsev on 27.05.14.
 */
public abstract class ChartDrawer {

    private float mMinYValue;
    private float mMaxYValue;
    private float[] mYValues;
    protected List<Rect> mRects = new ArrayList<Rect>();

    private List<String> mDates;

    private Paint mPaint = new Paint();
    private boolean mIsInited = false;
    protected boolean[] mSelectedPoints = new boolean[]{false, false, false, false, false, false};

    public ChartDrawer(){
        mPaint.setColor(Color.GRAY);
        mPaint.setTextSize(PxDpHelper.dpToPx(12));
        mPaint.setAntiAlias(true);
    }

    public void setSelectedPoints(boolean[] selectedPoints){
        mSelectedPoints = selectedPoints;
    }

    protected void resetRects(){
        mRects.clear();
    }

    protected void addRect(Rect rect){
        mRects.add(rect);
    }

    public int checkRectsHit(int x, int y){
        Rect rect;
        for(int i=0;i<mRects.size();i++){
            rect = mRects.get(i);
            if(rect.contains(x, y)) return i;
        }
        return -1;
    }

    protected float getChartHeight(int height){
        return height-PxDpHelper.dpToPx(30);
    }

    protected float getPointX(int point, int width){
        int total = mDates.size()+1;
        return width*(point+1)/total;
    }

    protected float yValueToCoordinate(float value, int height){
        float chartHeight = getChartHeight(height);
        return chartHeight - chartHeight* (value-mMinYValue)/(mMaxYValue-mMinYValue);
    }

    public void draw(Canvas canvas, int width, int height){
        if(!mIsInited) return;
        resetRects();
        float chartHeight = height- PxDpHelper.dpToPx(30);

        //draw horizontal axis
        {
            mPaint.setTextAlign(Paint.Align.RIGHT);
            float y;
            for(int i=0;i< mYValues.length;i++){
                y = chartHeight - chartHeight* (mYValues[i]-mMinYValue)/(mMaxYValue-mMinYValue);
                canvas.drawLine(1, y+1, width, y+1, mPaint);
                canvas.drawText(formatAxisValueString(mYValues[i], mMaxYValue - mMinYValue), width-10, y+25, mPaint);
            }
        }

        //draw vertical axis
        {
            mPaint.setTextAlign(Paint.Align.LEFT);
            float x;
            int total = mDates.size()+1;
            for(int i=0;i<mDates.size();i++){
                x = width*(i+1)/total;
                canvas.drawLine(x, 0, x, chartHeight, mPaint);
                drawMultiline(canvas, mDates.get(i), x - PxDpHelper.dpToPx(15), chartHeight - mPaint.ascent() + mPaint.descent(), mPaint);
            }
        }
    }

    private void drawMultiline(Canvas canvas, String str, float x, float y, Paint paint){
        int count = 0;
        for (String line: str.split("\n")){
            if(count>0){
                x+=20;
                Bitmap bitmap = BitmapFactory.decodeResource(YandexMetrikaApplication.getInstance().getResources(), R.drawable.multilines_label_wrap);
                canvas.drawBitmap(bitmap, x-bitmap.getWidth()-5, y-bitmap.getHeight()-PxDpHelper.dpToPx(2), paint);
            }
            canvas.drawText(line, x, y, paint);
            y += -paint.ascent() + paint.descent();
            count++;
        }
    }

    private String formatAxisValueString(float value, float delta){
        if(delta<10){
            return String.format("%.2f", value);
        }
        return String .format("%d", (int)value);
    }

    private float round(float value, float delta){
        float multiplier = getMultiplier(delta);
        return (Math.round(value*multiplier)/multiplier);
    }

    private float getMultiplier(float val){
        if(val>100) return getMultiplier(val / 10)/10;
        else if(val<10) return getMultiplier(val * 10)*10;
        return 1;
    }

    public void onDataChanged(ChartDataSource dataSource) {
        float delta = dataSource.getMaxValue() - dataSource.getMinValue();
        if(delta==0){
            mMinYValue = 0;
            mMaxYValue = dataSource.getMinValue()*2;
            mYValues = new float[]{mMinYValue, dataSource.getMinValue(), mMaxYValue};
        }else{
            float step;

            if(dataSource.getMinValue()==0){
                delta*=1.2f;
                step = round(delta/4, delta);
                mMinYValue = 0;
            }else{
                delta*=1.4f;
                step = round(delta/4, delta);
                mMinYValue = dataSource.getMinValue() - delta*0.2f;
                if(mMinYValue <0){
                    mMinYValue =0;
                }else{
                    mMinYValue = round(mMinYValue, delta);
                }
            }

            float val1 = mMinYValue;
            float val2 = mMinYValue +step;
            float val3 = val2+step;
            float val4 = val3+step;
            float val5 = mMaxYValue = val4+step;
            mYValues = new float[]{val1, val2, val3, val4, val5};
        }

        mDates = new ArrayList<String>();
        String date;
        DateInterval interval;
        SimpleDateFormat format = new SimpleDateFormat("dd.MM");
        for(int i=0;i<dataSource.getDateIntervals().size();i++){
            interval = dataSource.getDateIntervals().get(i);
            if(interval.getStartDate().equals(interval.getEndDate())){
                date = format.format(interval.getStartDate());
            }else{
                date = format.format(interval.getStartDate()) + "\n" + format.format(interval.getEndDate());
            }
            mDates.add(date);
        }



        mIsInited = true;
    }
}
