package com.jincong.simple.threadpoolsimple.model.bean;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolProxy {
    public static final int MAX_THREAD_NUM = 8;
    private static ThreadPoolProxy instance = null;
    private ThreadPoolExecutor mExecutor;
    public static ConcurrentHashMap<String, Boolean> isCanceled = new ConcurrentHashMap<>();

    public static ThreadPoolProxy getInstance() {
        if (instance == null) {
            synchronized (ThreadPoolProxy.class) {
                if (instance == null) {
                    instance = new ThreadPoolProxy();
                }
            }
        }
        return instance;
    }

    private void initThreadPoolExecutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()) {
                    long keepAliveTime = 3000;
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue workQueue = new LinkedBlockingQueue();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();

                    mExecutor = new ThreadPoolExecutor(MAX_THREAD_NUM, MAX_THREAD_NUM, keepAliveTime, unit, workQueue,
                            threadFactory, handler);
                }
            }
        }
    }

    public void execute(Runnable task) {
        initThreadPoolExecutor();
        mExecutor.execute(task);
    }

    public Future submit(Runnable task) {
        initThreadPoolExecutor();
        return mExecutor.submit(task);
    }

    public boolean remove(Future task) {
        if (!task.isCancelled()) {
            return task.cancel(true);
        }
        return false;
    }

    public void shutdownNow() {
        mExecutor.shutdownNow();
    }
}
