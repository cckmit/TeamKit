package org.team4u.base.lang;


import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 时间限制的聚合任务
 *
 * @author jay.wu
 */

public class TimedAggregationTask<T> extends LongTimeThread {

    @Getter
    private final int maxBufferSize;
    @Getter
    private final long timeoutMillis;
    @Getter
    private final Listener<T> listener;

    private final LinkedBlockingQueue<T> tasks;

    /**
     * @param maxBufferSize 每个批次最大数量
     * @param timeoutMillis 每个批次等待超时时间
     * @param listener      监听器
     */
    public TimedAggregationTask(int maxBufferSize, long timeoutMillis, Listener<T> listener) {
        this.maxBufferSize = maxBufferSize;
        this.timeoutMillis = timeoutMillis;
        this.listener = listener;

        tasks = new LinkedBlockingQueue<T>(maxBufferSize);
    }

    public TimedAggregationTask<T> add(T value) {
        try {
            tasks.put(value);
        } catch (InterruptedException e) {
            throw ExceptionUtil.wrapRuntime(e);
        }
        return this;
    }

    @Override
    protected void onRun() {
        List<T> buffer = poll();
        listener.onFlush(this, buffer);
    }

    private List<T> poll() {
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

            receive(buffer, value);
        }

        return buffer;
    }

    private void receive(List<T> buffer, T value) {
        buffer.add(value);
        listener.onReceive(this, value);
    }

    @Override
    protected Number runIntervalMillis() {
        return null;
    }

    public interface Listener<T> {

        /**
         * 新增数据时回调
         *
         * @param value 新增数据
         */
        void onReceive(TimedAggregationTask<T> task, T value);

        /**
         * 需要刷新数据时回调
         *
         * @param values 需要刷新的数据集合，若为空则表示超时仍然无数据
         */
        void onFlush(TimedAggregationTask<T> task, List<T> values);
    }
}