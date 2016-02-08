package ru.wwdi.metrika.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by ryashentsev on 30.04.14.
 */
public class AlphaContainer extends FrameLayout {

    private float mAlpha = 1;

    public AlphaContainer(Context context) {
        super(context);
        this.setWillNotDraw(false);
    }

    public AlphaContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
    }

    public AlphaContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setWillNotDraw(false);
    }

    public void setCustomAlpha(float alpha){
        mAlpha = alpha;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Rect rect = new Rect();
        getDrawingRect(rect);
        canvas.saveLayerAlpha(new RectF(rect), (int)(mAlpha*255), Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
        super.onDraw(canvas);
    }

}
