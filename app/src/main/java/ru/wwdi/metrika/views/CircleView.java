package ru.wwdi.metrika.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import ru.wwdi.metrika.helpers.PxDpHelper;

/**
 * Created by dmitrykorotchenkov on 14/04/14.
 */
public class CircleView extends View {

    private int mCircleColor;
    private Boolean mSelected;

    public CircleView(Context context){
        super(context);
        mCircleColor = Color.YELLOW;
        mSelected = false;
    }

    public CircleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mCircleColor = Color.YELLOW;
        mSelected = false;
    }

    public void update(int color, Boolean selected) {
        mCircleColor = color;
        mSelected = selected;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        Paint.Style style = mSelected ? Paint.Style.FILL_AND_STROKE : Paint.Style.STROKE;
        paint.setStyle(style);
        paint.setColor(mCircleColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, PxDpHelper.dpToPx(25) / 2 - 1, paint);
    }

}
