package org.team4u.kit.core.util;

import org.team4u.kit.core.error.ExceptionUtil;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadExUtil {

    /**
     * 一个便利的等待方法同步一个对象
     *
     * @param lock 锁对象
     * @param ms   要等待的时间 ms
     */
    public static void wait(Object lock, long ms) {
        if (null != lock)
            synchronized (lock) {
                try {
                    lock.wait(ms);
                } catch (InterruptedException e) {
                    throw ExceptionUtil.toRuntimeException(e);
                }
            }
    }

    /**
     * 通知对象的同步锁
     *
     * @param lock 锁对象
     */
    public static void notifyAll(Object lock) {
        if (null != lock)
            synchronized (lock) {
                lock.notifyAll();
            }
    }

    /**
     * 通知对象的同步锁
     *
     * @param lock 锁对象
     */
    public static void notify(Object lock) {
        if (null != lock)
            synchronized (lock) {
                lock.notify();
            }
    }

    public static Thread startThread(Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.start();
        return thread;
    }

    /**
     * 创建固定线程且固定任务的线程池
     *
     * @param maxThreads 最大线程数
     * @param maxBuffers 允许进入线程池的最大任务数,超出则阻塞
     */
    public static ThreadPoolExecutor newBlockingFixedThreadPool(int maxThreads, int maxBuffers) {
        return new ThreadPoolExecutor(
                0, maxThreads, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(maxBuffers),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    /**
     * 创建固定线程且固定任务的线程池
     *
     * @param maxThreads 最大线程数,超出则阻塞
     */
    public static ThreadPoolExecutor newBlockingFixedThreadPool(int maxThreads) {
        return new ThreadPoolExecutor(
                0, maxThreads, 0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

}