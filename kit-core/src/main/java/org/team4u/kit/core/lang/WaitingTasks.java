package org.team4u.kit.core.lang;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WaitingTasks<T> {

    private static final Log log = LogFactory.get();

    private final ReentrantLock putLock = new ReentrantLock();
    private final Condition full = putLock.newCondition();

    private final TimeMap<Object, WaitingTask<T>> tasks;

    private int limit;
    private long expirationSecs;

    public WaitingTasks(long expirationSecs) {
        this(0, expirationSecs);
    }

    public WaitingTasks(int limit, long expirationSecs) {
        this.limit = limit;
        this.expirationSecs = expirationSecs;
        tasks = new TimeMap<Object, WaitingTask<T>>(expirationSecs,
                new TimeMap.ExpiredCallback<Object, WaitingTask<T>>() {
                    @Override
                    public void expire(Object key, WaitingTask<T> waitingTask) {
                        signal();
                        waitingTask.finish(null);
                    }
                });
    }

    public WaitingTask<T> put(WaitingTask<T> task) throws TimeoutException {
        putLock.lock();

        try {
            if (limit > 0 && tasks.size() >= limit) {
                full.await(expirationSecs, TimeUnit.SECONDS);
                throw new TimeoutException("WaitingTasks full");
            }
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            putLock.unlock();
        }

        tasks.put(task.getId(), task);
        return task;
    }

    public WaitingTask<T> get(Object id) {
        return tasks.get(id);
    }

    public void finish(Object id, T body) {
        if (id == null) {
            return;
        }

        WaitingTask<T> waitingTask = get(id);

        if (waitingTask != null) {
            try {
                waitingTask.finish(body);
            } finally {
                tasks.remove(id);
                signal();
            }
        }
    }

    private void signal() {
        putLock.lock();
        try {
            full.signal();
        } finally {
            putLock.unlock();
        }
    }
}