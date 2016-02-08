package ru.wwdi.metrika.screens.traffic.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ru.wwdi.metrika.screens.traffic.TrafficScreen;
import ru.wwdi.metrika.webservice.responses.TrafficSummaryStatResponse;

/**
 * Created by ryashentsev on 14.05.14.
 */
public class Chart extends View implements ChartDataSource.ChartDataSourceListener{

    public static interface SelectListener{
        /**
         *
         * @param selectedPoints array of 6 elements each of which determines if appropriate column is selected: [true, true, true, true, false, false]
         */
        void onSelectionChanged(boolean[] selectedPoints);
    }

    public static enum ChartType{
        POINTS,
        COLUMNS
    }


    private TrafficSummaryStatResponse mData;
    private TrafficScreen.DataType mDataType;
    private ChartType mChartType;
    private ChartDataSource mDataSource;
    private SelectListener mListener;
    private ChartDrawer mDrawer;
    private boolean[] mSelectedPoints = new boolean[]{false, false, false, false, false, false};

    public Chart(Context context) {
        super(context);
        init();
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Chart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        //add data source listener
    }

    public void setSelectListener(SelectListener listener){
        mListener = listener;
    }

    public void setData(TrafficSummaryStatResponse data){
        mData = data;
        if(mDataSource!=null){
            mDataSource.setData(data);
        }
    }

    public void setDataType(TrafficScreen.DataType type){
        if(mDataType==type) return;
        mDataType = type;
        if(mDataSource!=null){
            mDataSource.setDataType(type);
        }
    }

    public void setViewType(ChartType type){
        if(mChartType==type) return;
        mChartType = type;
        if(mDataSource!=null) mDataSource.removeListener(this);
        mDataSource = type==ChartType.COLUMNS?new ColumnsChartDataSource():new PointsChartDataSource();
        mDrawer = type==ChartType.COLUMNS?new ColumnsChartDrawer():new PointsChartDrawer();
        mDrawer.setSelectedPoints(mSelectedPoints);
        mDataSource.addListener(this);
        if(mData!=null) mDataSource.setData(mData);
        if(mDataType!=null) mDataSource.setDataType(mDataType);
    }

    /*             PRIVATE              */


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(mDrawer!=null){
                int hitPoint = mDrawer.checkRectsHit((int)event.getX(), (int)event.getY());
                if(hitPoint>-1){
                    int firstSelectedPoint = getFirstSelectedPoint();
                    int lastSelectedPoint = getLastSelectedPoint();
                    if(firstSelectedPoint==-1){
                        mSelectedPoints[hitPoint] = true;
                    }else{
                        if(hitPoint<firstSelectedPoint){
                            //тыкнул слева от выделенных
                            for(int i=hitPoint;i<firstSelectedPoint;i++){
                                mSelectedPoints[i] = true;
                            }
                        }else if(hitPoint>lastSelectedPoint){
                            //тыкнул справа от выделенных
                            for(int i=lastSelectedPoint+1;i<=hitPoint;i++){
                                mSelectedPoints[i] = true;
                            }
                        }else if(hitPoint==lastSelectedPoint){
                            //тыкнул на самую правую выделенную
                            mSelectedPoints[hitPoint] = false;
                        }else if(hitPoint==firstSelectedPoint){
                            //тыкнул на самую левую выделенную
                            mSelectedPoints[hitPoint] = false;
                        }else{
                            //тыкнул внутри выделенных причем не с краю
                            for(int i=hitPoint+1;i<=lastSelectedPoint;i++){
                                mSelectedPoints[i] = false;
                            }
                        }
                    }

                    mDrawer.setSelectedPoints(mSelectedPoints);
                    mListener.onSelectionChanged(mSelectedPoints);
                    invalidate();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private int getFirstSelectedPoint(){
        for(int i=0;i<mSelectedPoints.length;i++){
            if(mSelectedPoints[i]) return i;
        }
        return -1;
    }

    private int getLastSelectedPoint(){
        for(int i=mSelectedPoints.length-1;i>=0;i--){
            if(mSelectedPoints[i]) return i;
        }
        return -1;
    }

    private boolean hasSelectedPoints(){
        for(boolean p: mSelectedPoints){
            if (p) return true;
        }
        return false;
    }

    @Override
    public void onDataChanged() {
        if(mDrawer!=null) mDrawer.onDataChanged(mDataSource);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDrawer!=null) mDrawer.draw(canvas, getWidth(), getHeight());
    }
}
