package org.team4u.base.lang;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 手工控制的长时线程
 *
 * @author jay.wu
 */
public abstract class ManuallyLongTimeThread extends LongTimeThread {

    private static final Log log = LogFactory.get();

    private int runCount = 0;

    public ManuallyLongTimeThread() {
    }

    public ManuallyLongTimeThread(Runnable target) {
        super(target);
    }

    public ManuallyLongTimeThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public ManuallyLongTimeThread(String name) {
        super(name);
    }

    public ManuallyLongTimeThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public ManuallyLongTimeThread(Runnable target, String name) {
        super(target, name);
    }

    public ManuallyLongTimeThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public ManuallyLongTimeThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public void run() {
        while (!isClosed()) {
            if (!canRun()) {
                ThreadUtil.safeSleep(runIntervalMillis());
                continue;
            }

            ++runCount;

            try {
                onRun();
            } catch (Exception e) {
                log.error(e, e.getMessage());
            }

            ThreadUtil.safeSleep(runIntervalMillis());
        }
    }

    private boolean canRun() {
        if (!isEnabled()) {
            return false;
        }

        if (maxRunCount() < 0) {
            return true;
        }

        return runCount < maxRunCount();
    }

    public int getRunCount() {
        return runCount;
    }

    /**
     * 获取是否开启任务
     *
     * @return 是否开启
     */
    protected abstract boolean isEnabled();

    /**
     * 获取最大运行次数
     *
     * @return 最大运行次数
     */
    protected abstract int maxRunCount();
}