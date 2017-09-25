package com.android.startup.protector;

import android.app.Application;
import android.os.Handler;
import android.content.Context;
import android.os.Looper;

import com.android.startup.protector.clear.ProtectorClearer;
import com.android.startup.protector.constant.SpConstant;
import com.android.startup.protector.handler.ProtectorHandler;
import com.android.startup.protector.util.ProtectorLogUtils;
import com.android.startup.protector.util.ProtectorSpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhao on 2017/9/22.
 */

public class Protector {
    private static Context context;
    private static Protector mProtector;
    private List<Runnable> userTasks = new ArrayList<>();// tasks user define
    private static final int firstLevel = 3;
    private static final int SecondLevel = 5;
    public boolean restartApp;

    private Protector() {
    }

    public static Protector getInstance() {
        if (mProtector == null) {
            synchronized (Protector.class) {
                if (mProtector == null) {
                    mProtector = new Protector();
                }
            }
        }
        return mProtector;
    }

    public Protector init(Application application) {
        context = application;
        ProtectorSpUtils.putInt(SpConstant.CRASHCONUT, ProtectorSpUtils.getInt(SpConstant.CRASHCONUT, 0) + 1);
        int countNow = ProtectorSpUtils.getInt(SpConstant.CRASHCONUT, 0);
        if (countNow > firstLevel) {
            ProtectorLogUtils.i("enter level one");
            for (Runnable runnable : userTasks) {
                if (runnable != null) {
                    runnable.run();
                }
            }

            if (countNow > SecondLevel) {
                // clear all and fix
                ProtectorLogUtils.i("enter level two");
                ProtectorClearer.clearAllFile(context);

                while(true){

                }

            }

        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                markSucceed();
            }
        }, 5000);
        Thread.setDefaultUncaughtExceptionHandler(new ProtectorHandler(Thread.getDefaultUncaughtExceptionHandler()));
        return this;
    }

    public Protector addTask(Runnable runnable) {
        userTasks.add(runnable);
        return this;
    }

    // mark as app lanuch successed
    public void markSucceed() {
        ProtectorSpUtils.putInt(SpConstant.CRASHCONUT, 0);
        ProtectorLogUtils.i("markSuceed");
    }

    // if try to restart app
    public void restart(boolean restart) {
        restartApp = restart;
    }

    public Protector setDebug(boolean isDebug) {
        ProtectorLogUtils.setDebug(isDebug);
        ProtectorLogUtils.i("StartUp-Protector debug : " + isDebug);
        return this;
    }

    public Context getContext() {
        return context;
    }


}
