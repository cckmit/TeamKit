package org.team4u.base.lang;


import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.Closeable;

/**
 * 长时间运行的线程
 *
 * @author jay.wu
 */
public abstract class LongTimeThread extends Thread implements Closeable {

    protected final Log log = LogFactory.get();

    private volatile boolean closed = false;

    public LongTimeThread() {
    }

    public LongTimeThread(Runnable target) {
        super(target);
    }

    public LongTimeThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public LongTimeThread(String name) {
        super(name);
    }

    public LongTimeThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public LongTimeThread(Runnable target, String name) {
        super(target, name);
    }

    public LongTimeThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public LongTimeThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public void run() {
        while (!closed) {
            if (isSleepBeforeRun()) {
                ThreadUtil.safeSleep(runIntervalMillis());
            }

            try {
                onRun();
            } catch (Exception e) {
                log.error(e, e.getMessage());
            }

            if (!isSleepBeforeRun()) {
                ThreadUtil.safeSleep(runIntervalMillis());
            }
        }
        log.info("Thread is closed");
    }

    @Override
    public void close() {
        closed = true;
        onClose();
    }

    /**
     * 阻塞直到线程关闭
     */
    public void awaitTermination() {
        ThreadUtil.waitForDie(this);
    }

    /**
     * 是否关闭
     *
     * @return true：关闭，false：未关闭
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * 循环执行方法
     */
    protected abstract void onRun();

    /**
     * 运行间隔
     *
     * @return 运行间隔（毫秒）
     */
    protected abstract Number runIntervalMillis();

    /**
     * 关闭处理方法
     */
    protected void onClose() {
    }

    /**
     * 是否在运行之前休眠
     */
    protected boolean isSleepBeforeRun() {
        return false;
    }
}