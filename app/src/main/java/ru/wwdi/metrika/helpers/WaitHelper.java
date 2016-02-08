package ru.wwdi.metrika.helpers;

import android.os.Handler;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: ryashentsev
 * Date: 26.11.12
 * Time: 13:05
 * To change this template use File | Settings | File Templates.
 */
public class WaitHelper {

    private static Executor sExecutor = Executors.newSingleThreadExecutor();

    public static void waitAndExecute(final int millis, final Runnable task){
        final Handler handler = new Handler();
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                }
                handler.post(task);
            }
        });
    }

}
