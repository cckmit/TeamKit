package org.team4u.base.lang;


import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 聚合任务
 *
 * @author jay.wu
 */

public class AggregationTask<T> extends LongTimeThread {

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
    public AggregationTask(int maxBufferSize, long timeoutMillis, Listener<T> listener) {
        this.maxBufferSize = maxBufferSize;
        this.timeoutMillis = timeoutMillis;
        this.listener = listener;

        tasks = new LinkedBlockingQueue<T>(maxBufferSize);
    }

    public AggregationTask<T> add(T value) {
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

        if (buffer.isEmpty()) {
            listener.onEmpty(this);
            return;
        }

        listener.onFull(this, buffer);
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

            buffer.add(value);
            listener.onAdd(this, value);
        }

        return buffer;
    }

    @Override
    protected Number runIntervalMillis() {
        return null;
    }

    public interface Listener<T> {

        /**
         * 超时无数据时回调
         */
        void onEmpty(AggregationTask<T> task);

        /**
         * 每次新增数据时回调
         *
         * @param value 新增数据
         */
        void onAdd(AggregationTask<T> task, T value);

        /**
         * 已满一个批次时回调
         *
         * @param values 一个批次集合
         */
        void onFull(AggregationTask<T> task, List<T> values);
    }
}