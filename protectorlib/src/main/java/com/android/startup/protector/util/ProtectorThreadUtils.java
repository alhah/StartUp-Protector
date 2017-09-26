package com.android.startup.protector.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by liuzhao on 2017/9/26.
 * <p>
 * the count is copied from android/os/AsyncTask.java
 */

public class ProtectorThreadUtils {

    private static ProtectorThreadUtils mProtectorThreadUtils;
    private static ThreadPoolExecutor mExecutor;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 30;


    private ProtectorThreadUtils() {
    }

    public static ProtectorThreadUtils getInstance() {
        if (mProtectorThreadUtils == null) {
            synchronized (ProtectorThreadUtils.class) {
                if (mProtectorThreadUtils == null) {
                    mProtectorThreadUtils = new ProtectorThreadUtils();
                    mExecutor = new ThreadPoolExecutor(
                            CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>());
                }
            }
        }
        return mProtectorThreadUtils;
    }

    public void execute(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}