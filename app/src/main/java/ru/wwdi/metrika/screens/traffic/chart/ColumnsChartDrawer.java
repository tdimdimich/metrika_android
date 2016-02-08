package ru.wwdi.metrika.screens.traffic.chart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.List;

import ru.wwdi.metrika.helpers.SharedPreferencesHelper;

/**
 * Created by ryashentsev on 28.05.14.
 */
public class ColumnsChartDrawer extends ChartDrawer {

    private Paint mPaint = new Paint();
    private List<List<Float>> mColumns;

    public ColumnsChartDrawer(){
        mPaint.setColor(SharedPreferencesHelper.getSelectedCounter().getColor());
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDataChanged(ChartDataSource dataSource) {
        super.onDataChanged(dataSource);
        ColumnsChartDataSource pDataSource = (ColumnsChartDataSource) dataSource;
        mColumns = pDataSource.getColumns();
    }

    @Override
    public void draw(Canvas canvas, int width, int height) {
        super.draw(canvas, width, height);
        int bigColumnWidth;
        if(mColumns.size()==1){
            bigColumnWidth = width/3;
        }else{
            bigColumnWidth = (int) ((getPointX(1, width) - getPointX(0, width))*0.8);
        }

        int smallColumnWidth;
        List<Float> bigColumn;
        float smallColumn;
        int barY;
        int barX;
        int charHeight = (int) getChartHeight(height);
        Rect bigColumnRect;
        for(int i=0;i<mColumns.size();i++){
            barX = (int) (getPointX(i, width) - bigColumnWidth/2);
            bigColumn = mColumns.get(i);
            smallColumnWidth = bigColumnWidth/bigColumn.size();
            bigColumnRect = new Rect();
            for(int j=0;j<bigColumn.size();j++){
                smallColumn = bigColumn.get(j);
                barY = (int) yValueToCoordinate(smallColumn, height);

                int color = mSelectedPoints[i]?Color.WHITE:SharedPreferencesHelper.getSelectedCounter().getColor();
                mPaint.setColor(color);
                canvas.drawRect(barX, barY, barX+smallColumnWidth, charHeight, mPaint);
                bigColumnRect.union(barX, barY, barX+smallColumnWidth, charHeight);
                barX+=smallColumnWidth;
            }
            addRect(bigColumnRect);
        }
    }
}
