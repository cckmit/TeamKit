package org.team4u.kit.core.lang;

import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.xiaoleilu.hutool.thread.ThreadUtil;

import java.io.Closeable;

/**
 * @author Jay Wu
 */
public abstract class LongTimeThread extends Thread implements Closeable {

    private static final Log log = LogFactory.get();

    private boolean closed = false;

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
            try {
                onRun();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void close() {
        closed = true;
        onClose();
    }

    public void awaitTermination() {
        ThreadUtil.waitForDie(this);
    }

    public boolean isClosed() {
        return closed;
    }

    protected abstract void onRun();

    protected void onClose() {
    }
}