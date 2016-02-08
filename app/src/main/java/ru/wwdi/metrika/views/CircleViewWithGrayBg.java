package ru.wwdi.metrika.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import ru.wwdi.metrika.helpers.PxDpHelper;

/**
 * Created by dmitrykorotchenkov on 14/04/14.
 */
public class CircleViewWithGrayBg extends CircleView {

    private boolean mIsGrayBgVisible;

    public CircleViewWithGrayBg(Context context){
        super(context);
    }

    public CircleViewWithGrayBg(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setGrayBgVisible(boolean visible){
        mIsGrayBgVisible = visible;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(mIsGrayBgVisible){
            Paint paint = new Paint();
            Paint.Style style = Paint.Style.FILL;
            paint.setStyle(style);
            paint.setColor(Color.parseColor("#eeeeee"));
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, PxDpHelper.dpToPx(35) / 2 - 1, paint);
        }
        super.onDraw(canvas);
    }

}
