package ru.wwdi.metrika.topPanel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.util.CustomAnimator;
import ru.wwdi.metrika.views.AlphaContainer;
import ru.wwdi.metrika.views.ScrollViewWithDisablableScrolling;

/**
 * Created by ryashentsev on 29.04.14.
 */
public class SlideDownPanel extends LinearLayout {

    private AlphaContainer mSmallPanelAlphaContainer;
    private View mSmallPanel;
    private AlphaContainer mBigPanelAlphaContainer;
    private View mBigPanel;
    protected View mMinimize;
    private ScrollViewWithDisablableScrolling mScrollView;

    private GestureDetector mGestureDetector;
    private float mStartY;
    private int mStartHeight;
    private int mMinHeight;
    private int mMaxHeight;
    private CustomAnimator mAnimation;

    public SlideDownPanel(Context context) {
        super(context);
        init();
    }

    public SlideDownPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SlideDownPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void move(float y){
        ViewGroup.LayoutParams lp = getLayoutParams();
        int height = (int) (mStartHeight+y-mStartY);
        if(height<=mMinHeight){
            height = mMinHeight;
            mBigPanelAlphaContainer.setVisibility(INVISIBLE);
        }else if(height>=mMaxHeight){
            height = mMaxHeight;
            mSmallPanelAlphaContainer.setVisibility(INVISIBLE);
        }else{
            mBigPanelAlphaContainer.setVisibility(VISIBLE);
            mSmallPanelAlphaContainer.setVisibility(VISIBLE);
        }
        lp.height = height;
        setLayoutParams(lp);
        mScrollView.scrollTo(0, 5000);
        post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 5000);
            }
        });
        float alpha = getAlpha(height);
        mBigPanelAlphaContainer.setCustomAlpha(alpha);
        mSmallPanelAlphaContainer.setCustomAlpha(1 - alpha);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = mMinHeight;
        setLayoutParams(lp);
    }

    private void init(){
        mGestureDetector = new GestureDetector(this.getContext(), new UpDownGestureDetector());
        LayoutInflater.from(getContext()).inflate(R.layout.top_panel, this);
        mSmallPanelAlphaContainer = (AlphaContainer) findViewById(R.id.smallPanelAlphaContainer);
        mSmallPanel = findViewById(R.id.smallPanel);
        mSmallPanel.measure(MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(1, MeasureSpec.UNSPECIFIED));
        mBigPanelAlphaContainer = (AlphaContainer) findViewById(R.id.bigPanelAlphaContainer);
        mBigPanel = findViewById(R.id.bigPanel);


        OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!isEnabled()) return false;
                mGestureDetector.onTouchEvent(event);
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    initMaxHeight();
                    mStartY = event.getRawY();
                    mStartHeight = getHeight();
                }else if(event.getAction()==MotionEvent.ACTION_MOVE){
                    move(event.getRawY());
                }else if(event.getAction()==MotionEvent.ACTION_UP){
                    if(mAnimation==null){
                        int height = (int) (mStartHeight+event.getRawY()-mStartY);
                        if(1f*(height-mMinHeight)/(mMaxHeight-mMinHeight)<0.5f){
                            hide();
                        }else{
                            show();
                        }
                    }
                }
                return true;
            }
        };
        mSmallPanel.setOnTouchListener(onTouchListener);
        mMinimize = findViewById(R.id.minimize);
        mMinimize.setOnTouchListener(onTouchListener);
        mMinHeight = mSmallPanel.getMeasuredHeight();
        mScrollView = (ScrollViewWithDisablableScrolling) findViewById(R.id.scrollView);
        mBigPanelAlphaContainer.setCustomAlpha(0);
    }

    private void initMaxHeight(){
        mBigPanel.measure(MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(1, MeasureSpec.UNSPECIFIED));
        mMaxHeight = mBigPanel.getMeasuredHeight();
    }

    public void hide(){
        if(!isEnabled()) return;
        if(mAnimation!=null){
            mAnimation.stop();
            mAnimation=null;
        }
        mStartHeight = getHeight();
        mSmallPanelAlphaContainer.setVisibility(VISIBLE);
        mBigPanelAlphaContainer.setVisibility(VISIBLE);
        mAnimation = new CustomAnimator(200, new CustomAnimator.CustomAnimatorListener() {
            @Override
            public void onDoStep(float partOf1) {
                ViewGroup.LayoutParams lp = getLayoutParams();
                int height = (int) (mStartHeight + (mMinHeight-mStartHeight)*partOf1);
                lp.height = height;
                setLayoutParams(lp);
                mScrollView.scrollTo(0, 5000);
                if(partOf1<1){
                    float alpha = getAlpha(height);
                    mBigPanelAlphaContainer.setCustomAlpha(alpha);
                    mSmallPanelAlphaContainer.setCustomAlpha(1-alpha);
                    mSmallPanelAlphaContainer.setVisibility(VISIBLE);
                    mBigPanelAlphaContainer.setVisibility(VISIBLE);
                }else{
                    mBigPanelAlphaContainer.setCustomAlpha(0);
                    mBigPanelAlphaContainer.setVisibility(INVISIBLE);
                    mSmallPanelAlphaContainer.setVisibility(VISIBLE);

                    mSmallPanelAlphaContainer.setCustomAlpha(1);
                    mAnimation = null;
                }
            }
        });
        mAnimation.start();
    }

    public void show(){
        if(!isEnabled()) return;
        if(mAnimation!=null){
            mAnimation.stop();
            mAnimation=null;
        }
        initMaxHeight();
        mStartHeight = getHeight();
        mBigPanelAlphaContainer.setVisibility(VISIBLE);
        mSmallPanelAlphaContainer.setVisibility(VISIBLE);
        mAnimation = new CustomAnimator(200, new CustomAnimator.CustomAnimatorListener() {
            @Override
            public void onDoStep(float partOf1) {
                ViewGroup.LayoutParams lp = getLayoutParams();
                int height = (int) (mStartHeight + (mMaxHeight-mStartHeight)*partOf1);
                lp.height = height;
                setLayoutParams(lp);
                mScrollView.scrollTo(0, 5000);
                if(partOf1<1){
                    float alpha = getAlpha(height);
                    mBigPanelAlphaContainer.setCustomAlpha(alpha);
                    mSmallPanelAlphaContainer.setCustomAlpha(1-alpha);
                    mSmallPanelAlphaContainer.setVisibility(VISIBLE);
                    mBigPanelAlphaContainer.setVisibility(VISIBLE);
                }else{
                    mBigPanelAlphaContainer.setCustomAlpha(1);
                    mSmallPanelAlphaContainer.setVisibility(INVISIBLE);
                    mBigPanelAlphaContainer.setVisibility(VISIBLE);
                    mSmallPanelAlphaContainer.setCustomAlpha(0);
                    mAnimation = null;
                }
            }
        });
        mAnimation.start();
    }

    private float getAlpha(int height){
        return 1f*(height-mMinHeight)/(mMaxHeight-mMinHeight);
    }




    class UpDownGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 1;
        private static final int SWIPE_MAX_OFF_PATH = 250;
        private static final int SWIPE_THRESHOLD_VELOCITY = 1;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH){
                    return false;
                }
                // right to left swipe
                if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    hide();
                }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    show();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(getHeight()==mMinHeight){
                show();
            }else if(getHeight()==mMaxHeight){
                hide();
            }
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
