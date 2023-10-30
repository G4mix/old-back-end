package com.gamix.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static final int THREAD_POOL_SIZE = 1;

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(THREAD_POOL_SIZE);

    public static void schedule(Runnable task, long delay, TimeUnit unit) {
        scheduler.schedule(task, delay, unit);
    }
}
