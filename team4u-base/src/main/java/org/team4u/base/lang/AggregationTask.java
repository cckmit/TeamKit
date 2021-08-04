package org.team4u.base.lang;


import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.lang.func.VoidFunc1;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AggregationTask<T> implements Closeable {

    private static final Log log = LogFactory.get();
    private final ExecutorService threadExecutor = Executors.newSingleThreadExecutor();
    private boolean isClosed = false;
    private LinkedBlockingQueue<T> tasks;
    private VoidFunc1<List<T>> worker;
    private int maxBufferSize = 1;
    private long timeoutMillis = 1000;

    public AggregationTask<T> start() {
        if (!isClosed) {
            throw new IllegalStateException("BufferTask already closed");
        }

        tasks = new LinkedBlockingQueue<T>(maxBufferSize);

        threadExecutor.execute(() -> {
            while (!isClosed || !tasks.isEmpty()) {
                execute();
            }
        });

        return this;
    }

    public AggregationTask<T> put(T value) {
        try {
            tasks.put(value);
        } catch (InterruptedException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
        return this;
    }

    private void execute() {
        List<T> buffer = new ArrayList<>();

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
            worker.call(buffer);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public VoidFunc1<List<T>> getWorker() {
        return worker;
    }

    public AggregationTask<T> setWorker(VoidFunc1<List<T>> worker) {
        this.worker = worker;
        return this;
    }

    public long getTimeoutMillis() {
        return timeoutMillis;
    }

    public AggregationTask<T> setTimeout(long timeout, TimeUnit unit) {
        this.timeoutMillis = unit.toMillis(timeout);
        return this;
    }

    public int getMaxBufferSize() {
        return maxBufferSize;
    }

    public AggregationTask<T> setMaxBufferSize(int maxBufferSize) {
        this.maxBufferSize = maxBufferSize;
        return this;
    }

    public void awaitTermination(long awaitTerminationTimeout, TimeUnit unit) {
        try {
            //noinspection ResultOfMethodCallIgnored
            threadExecutor.awaitTermination(awaitTerminationTimeout, unit);
        } catch (InterruptedException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    @Override
    public void close() {
        isClosed = true;
        threadExecutor.shutdown();
    }
}