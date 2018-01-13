package org.team4u.kit.core.lang;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.error.ExceptionUtil;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BufferTask<T> implements Closeable {

    private static final Log log = LogFactory.get();

    private LinkedBlockingQueue<T> tasks;
    private boolean isClosed = false;

    private int maxBufferSize = 100;
    private long timeoutMillis = 1000;
    private Callback<List<T>> worker;

    private ExecutorService threadExecutor = Executors.newSingleThreadExecutor();

    public BufferTask<T> start() {
        if (isClosed) {
            throw new IllegalStateException("BufferTask already closed");
        }

        tasks = new LinkedBlockingQueue<T>(maxBufferSize);

        threadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (!isClosed || !tasks.isEmpty()) {
                    execute();
                }
            }
        });

        return this;
    }

    public BufferTask<T> put(T value) {
        try {
            tasks.put(value);
        } catch (InterruptedException e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
        return this;
    }

    private void execute() {
        List<T> buffer = new ArrayList<T>();

        while (buffer.size() < maxBufferSize) {
            T value;

            try {
                value = tasks.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                break;
            }

            if (value == null) {
                break;
            }

            buffer.add(value);
        }

        if (buffer.isEmpty()) {
            return;
        }

        try {
            worker.invoke(buffer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public Callback<List<T>> getWorker() {
        return worker;
    }

    public BufferTask<T> setWorker(Callback<List<T>> worker) {
        this.worker = worker;
        return this;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public BufferTask<T> setTimeout(long timeout, TimeUnit unit) {
        this.timeoutMillis = unit.toMillis(timeout);
        return this;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public BufferTask<T> setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
        return this;
    }

    public void awaitTermination(long awaitTerminationTimeout, TimeUnit unit) {
        try {
            threadExecutor.awaitTermination(awaitTerminationTimeout, unit);
        } catch (InterruptedException e) {
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

    @Override
    public void close() {
        isClosed = true;
        threadExecutor.shutdown();
    }
}