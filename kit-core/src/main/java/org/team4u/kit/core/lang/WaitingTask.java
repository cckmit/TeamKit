package org.team4u.kit.core.lang;


import org.team4u.kit.core.action.Callback;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WaitingTask<T> implements Future<T> {

    public static final long DEFAULT_TIMEOUT_SECS = 30;

    private boolean cancel = false;
    private boolean done = false;

    private Object id;
    private T body;
    private Callback<T> callback;

    public WaitingTask() {
    }

    public WaitingTask(Object id) {
        this.id = id;
    }

    public WaitingTask(Object id, T body) {
        this.id = id;
        this.body = body;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        cancel = true;
        notify();

        return cancel;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public synchronized T get(long timeout, TimeUnit unit) {
        if (done || cancel) {
            return body;
        }

        try {
            if (timeout != 0 && unit != null) {
                wait(unit.toMillis(timeout));
            } else {
                wait();
            }
        } catch (InterruptedException e) {
            // Ignore error
        }

        return body;
    }

    @Override
    public T get() {
        return get(DEFAULT_TIMEOUT_SECS, TimeUnit.SECONDS);
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public synchronized void finish(T body) {
        if (done || cancel) {
            return;
        }

        done = true;
        this.body = body;

        if (callback != null) {
            callback.invoke(body);
        }

        notifyAll();
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public void setCallback(Callback<T> callback) {
        this.callback = callback;
    }
}