package ru.wwdi.metrika.screens.base;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.util.CustomAnimator;

/**
 * Created by ryashentsev on 15.05.14.
 */
public class SourceLine extends FrameLayout {

    public static class LineData{
        private String mTitle;
        private String mValue;
        private List<SourceSubline.SublineData> msublineData;
        public LineData(String title, String value, List<SourceSubline.SublineData> sublineData){
            mTitle = title;
            mValue = value;
            msublineData = sublineData;
        }
        public String getTitle() {
            return mTitle;
        }
        public String getValue() {
            return mValue;
        }
        public List<SourceSubline.SublineData> getSublineData() {
            return msublineData;
        }
    }

    private TextView mTitle;
    private TextView mValue;
    private View mPoints;
    private View mArrow;
    private View mDivider;

    private View mSublinesContainer;
    private SourceSubline mSubline1;
    private SourceSubline mSubline2;
    private SourceSubline mSubline3;
    private SourceSubline mSubline4;
    private SourceSubline mSubline5;
    private SourceSubline mSubline6;
    private SourceSubline[] mSublines;

    private CustomAnimator mAnimation;
    private boolean mIsOpened = false;
    private boolean mShowSublines = true;

    public SourceLine(Context context) {
        super(context);
        init();
    }

    public SourceLine(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceLine(Context context, AttributeSet attrs, int defStyle) {
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

    public void setData(int color, LineData data){
        mTitle.setText(Html.fromHtml(data.getTitle()));
        mValue.setText(data.getValue());
        for(int i=0;i<mSublines.length;i++){
            if(data.getSublineData().size()>i){
                mSublines[i].setData(color, data.getSublineData().get(i));
                mSublines[i].setVisibility(VISIBLE);
            }else{
                mSublines[i].setVisibility(GONE);
            }
        }
        mValue.setTextColor(color);
        mDivider.setBackgroundColor(color);
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.source_site_line, this);
        mTitle = (TextView) findViewById(R.id.title);
        mValue = (TextView) findViewById(R.id.value);
        mArrow = findViewById(R.id.arrow);
        mPoints = findViewById(R.id.points);
        mSublinesContainer = findViewById(R.id.sublinesContainer);

        mSubline1 = (SourceSubline) findViewById(R.id.subline1);
        mSubline2 = (SourceSubline) findViewById(R.id.subline2);
        mSubline3 = (SourceSubline) findViewById(R.id.subline3);
        mSubline4 = (SourceSubline) findViewById(R.id.subline4);
        mSubline5 = (SourceSubline) findViewById(R.id.subline5);
        mSubline6 = (SourceSubline) findViewById(R.id.subline6);
        mSublines = new SourceSubline[]{mSubline1, mSubline2, mSubline3, mSubline4, mSubline5, mSubline6};
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
}
