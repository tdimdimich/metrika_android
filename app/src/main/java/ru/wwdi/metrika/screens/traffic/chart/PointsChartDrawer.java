package ru.wwdi.metrika.screens.traffic.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;

import java.util.List;

import ru.wwdi.metrika.helpers.PxDpHelper;
import ru.wwdi.metrika.helpers.SharedPreferencesHelper;

/**
 * Created by ryashentsev on 28.05.14.
 */
public class PointsChartDrawer extends ChartDrawer {

    private Paint mMainPaint = new Paint();
    private Paint mFillPaint = new Paint();
    private List<Float> mPoints;
    private int mOuterCircleRadius = PxDpHelper.dpToPx(6);
    private int mInnerCircleRadius = PxDpHelper.dpToPx(3);
    private int mHitRectWidth = PxDpHelper.dpToPx(20);

    public PointsChartDrawer(){
        mMainPaint.setColor(SharedPreferencesHelper.getSelectedCounter().getColor());
        float strokeWidth = PxDpHelper.dpToPx(7);
        mMainPaint.setStyle(Paint.Style.FILL);
        mMainPaint.setStrokeWidth(strokeWidth);

        mFillPaint.setColor(Color.parseColor("#464749"));
        mFillPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDataChanged(ChartDataSource dataSource) {
        super.onDataChanged(dataSource);
        PointsChartDataSource pDataSource = (PointsChartDataSource) dataSource;
        mPoints = pDataSource.getPoints();
    }

    @Override
    public void draw(Canvas canvas, int width, int height) {
        super.draw(canvas, width, height);
        float x=0;
        float y=0;
        float prevX=0;
        float prevY=0;
        //draw lines
        for(int i=0;i<mPoints.size();i++){
            if(i>0){
                prevX = x;
                prevY = y;
            }
            x = getPointX(i, width);
            y = yValueToCoordinate(mPoints.get(i), height);
            if(i>0){
                boolean selected = mSelectedPoints[i]&&mSelectedPoints[i-1];
                int color1;
                int color2;
                if(selected){
                    color1 = Color.WHITE;
                    color2 = Color.LTGRAY;
                }else{
                    color1 = SharedPreferencesHelper.getSelectedCounter().getColor();
                    color2 = SharedPreferencesHelper.getSelectedCounter().getGradientColor();
                }
                mMainPaint.setShader(new LinearGradient(prevX, prevY, x, y, color1, color2, Shader.TileMode.CLAMP));
                canvas.drawLine(prevX, prevY, x, y, mMainPaint);
                mMainPaint.setShader(null);
            }
        }
        //draw points stroke
        for(int i=0;i<mPoints.size();i++){
            x = getPointX(i, width);
            y = yValueToCoordinate(mPoints.get(i), height);
            int color = mSelectedPoints[i]?Color.WHITE:SharedPreferencesHelper.getSelectedCounter().getColor();
            mMainPaint.setColor(color);
            canvas.drawCircle(x, y, mOuterCircleRadius, mMainPaint);
            addRect(new Rect((int)(x-mHitRectWidth/2), (int)(y-mHitRectWidth/2), (int)(x+mHitRectWidth/2), (int)(y+mHitRectWidth/2)));
        }
        //draw points fill
        for(int i=0;i<mPoints.size();i++){
            x = getPointX(i, width);
            y = yValueToCoordinate(mPoints.get(i), height);
            canvas.drawCircle(x, y, mInnerCircleRadius, mFillPaint);
        }
    }
}
