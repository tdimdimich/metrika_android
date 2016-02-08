package ru.wwdi.metrika.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import ru.wwdi.metrika.R;
import ru.wwdi.metrika.util.CustomAnimator;

/**
 * Created by ryashentsev on 28.04.14.
 */
public class MenuItemContainerView extends MenuItemView {

    private LinearLayout mChildrenContainer;
    private CustomAnimator mAnimation;
    private View mMore;
    private View mLess;

    public MenuItemContainerView(Context context) {
        super(context);
    }

    public MenuItemContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public MenuItemContainer getMenuItem() {
        return (MenuItemContainer) super.getMenuItem();
    }

    @Override
    protected void onFinishInflate() {
        mChildrenContainer = (LinearLayout) findViewById(R.id.childrenContainer);
        mMore = findViewById(R.id.more);
        mLess = findViewById(R.id.less);
        mMore.setVisibility(VISIBLE);
        mLess.setVisibility(INVISIBLE);
        close(false);
        super.onFinishInflate();
    }

    private int getChildContainerHeight(){
        mChildrenContainer.measure(MeasureSpec.makeMeasureSpec(mChildrenContainer.getWidth(),MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(1,MeasureSpec.UNSPECIFIED));
        return mChildrenContainer.getMeasuredHeight();
    }

    public void open(boolean animated){
        if(mAnimation!=null){
            mAnimation.stop();
            mAnimation = null;
        }
        if(animated){
            animateChildContainer(0, getChildContainerHeight());
        }else{
            LayoutParams lp = (LayoutParams) mChildrenContainer.getLayoutParams();
            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            mChildrenContainer.setLayoutParams(lp);
        }

        AlphaAnimation a = new AlphaAnimation(0, 1);
        a.setDuration(400);
        mLess.setVisibility(VISIBLE);
        mLess.startAnimation(a);
        a = new AlphaAnimation(1, 0);
        a.setDuration(400);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                mMore.setVisibility(INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mMore.startAnimation(a);
    }

    public void close(boolean animated){
        if(mAnimation!=null){
            mAnimation.stop();
            mAnimation = null;
        }
        if(animated){
            animateChildContainer(mChildrenContainer.getLayoutParams().height, 0);
        }else{
            LayoutParams lp = (LayoutParams) mChildrenContainer.getLayoutParams();
            lp.height = 0;
            mChildrenContainer.setLayoutParams(lp);
        }

        AlphaAnimation a = new AlphaAnimation(1, 0);
        a.setDuration(400);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLess.setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mLess.startAnimation(a);
        a = new AlphaAnimation(0, 1);
        a.setDuration(400);
        mMore.setVisibility(VISIBLE);
        mMore.startAnimation(a);
    }

    private void animateChildContainer(final int startHeight, final int finishHeight){
        mAnimation = new CustomAnimator((int) (Math.abs(finishHeight - startHeight)/1.5f), new CustomAnimator.CustomAnimatorListener() {
            @Override
            public void onDoStep(float partOf1) {
                LayoutParams params = (LayoutParams) mChildrenContainer.getLayoutParams();
                params.height = (int) (startHeight + (finishHeight - startHeight)*(partOf1));
                mChildrenContainer.setLayoutParams(params);
            }
        });
        mAnimation.start();
    }

    @Override
    public void setMenuItemClickListener(MenuItemClickListener listener) {
        mListener = listener;
        findViewById(R.id.top_line).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(MenuItemContainerView.this);
            }
        });

        for(int i=0;i<mChildrenContainer.getChildCount();i++){
            View v = mChildrenContainer.getChildAt(i);
            if(v instanceof MenuItemView){
                ((MenuItemView)v).setMenuItemClickListener(listener);
            }
        }
    }

    public void setMenuItem(MenuItemContainer item){
        super.setMenuItem(item);
        MenuItemView childView;
        for(MenuItem childItem: item.getChildren()){
            childView = (MenuItemView) LayoutInflater.from(getContext()).inflate(R.layout.menu_item, null);
            childView.setMenuItemClickListener(mListener);
            childView.setMenuItem(childItem);
            childView.findViewById(R.id.dotted_line).setBackgroundResource(R.drawable.dotted_line_dark_gray);
            mChildrenContainer.addView(childView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
    }
}
