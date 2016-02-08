package ru.wwdi.metrika.util;

import android.os.Handler;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by ryashentsev on 29.04.14.
 */
public class CustomAnimator {

    private static final int STEP_IN_MILLIS = 30;

    private Handler mHandler;
    private int mDuration;
    private CustomAnimatorListener mListener;
    private Thread mThread;
    private Interpolator mInterpolator = new DecelerateInterpolator();

    public CustomAnimator(int durationMillis, CustomAnimatorListener listener){
        mHandler = new Handler();
        mDuration = durationMillis;
        mListener = listener;
    }

    public void stop(){
        if(mThread==null) return;
        mThread.interrupt();
    }

    private void dispatchDoStep(final float partOf1){
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onDoStep(mInterpolator.getInterpolation(partOf1));
            }
        });
    }

    public void start(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int millisPassed = 0;
                millisPassed+=STEP_IN_MILLIS;
                float partOf1 = 1f*millisPassed/mDuration;
                dispatchDoStep(partOf1);
                while(millisPassed<mDuration){
                    try {
                        Thread.sleep(STEP_IN_MILLIS);
                    } catch (InterruptedException e) {
                    }
                    if(mThread.isInterrupted()) return;
                    millisPassed+=STEP_IN_MILLIS;
                    partOf1 = 1f*millisPassed/mDuration;
                    dispatchDoStep(partOf1);
                }
                dispatchDoStep(1);
            }
        };
        mThread = new Thread(r);
        mThread.start();
    }


    public static interface CustomAnimatorListener {
        public void onDoStep(float partOf1);
    }

}
