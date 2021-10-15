package org.team4u.base.lang.aggregation;


import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.Getter;
import org.team4u.base.lang.LongTimeThread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 时间限制的聚合任务
 *
 * @author jay.wu
 */

public class TimedAggregationTask<T> extends AbstractAggregationTask<T> {

    @Getter
    private final long timeoutMillis;

    private final Worker worker;
    private final LinkedBlockingQueue<T> tasks;

    /**
     * @param maxBufferSize 每个批次最大数量
     * @param timeoutMillis 每个批次等待超时时间
     * @param listener      监听器
     */
    public TimedAggregationTask(int maxBufferSize, long timeoutMillis, AggregationTaskListener<T> listener) {
        super(maxBufferSize, listener);
        this.timeoutMillis = timeoutMillis;

        tasks = new LinkedBlockingQueue<T>(maxBufferSize);

        this.worker = new Worker();
        worker.start();
    }

    public void add(T value) {
        try {
            tasks.put(value);
        } catch (InterruptedException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
    }

    private void poll() {
        List<T> buffer = new ArrayList<>();

        while (buffer.size() < getMaxBufferSize()) {
            T value;

            try {
                value = tasks.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                break;
            }

            if (value == null) {
                break;
            }

            receive(buffer, value);
        }

        flush(buffer);
    }

    private void flush(List<T> buffer) {
        if (buffer.isEmpty()) {
            return;
        }

        getListener().onFlush(this, buffer);

        getStatistic().incrementFlushSize(buffer.size());
    }

    private void receive(List<T> buffer, T value) {
        buffer.add(value);

        getListener().onReceive(this, value);

        getStatistic().incrementReceiveSize();
    }

    @Override
    public void close() throws IOException {
        worker.close();
    }

    private class Worker extends LongTimeThread {

        @Override
        protected void onRun() {
            poll();
        }

        @Override
        protected Number runIntervalMillis() {
            return null;
        }
    }
}