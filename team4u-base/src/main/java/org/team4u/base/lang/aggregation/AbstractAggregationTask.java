package org.team4u.base.lang.aggregation;


import lombok.Getter;

import java.io.Closeable;

/**
 * 聚合任务
 *
 * @author jay.wu
 */

public abstract class AbstractAggregationTask<T> implements Closeable {
    @Getter
    private final int maxBufferSize;
    @Getter
    private final AggregationTaskListener<T> listener;
    @Getter
    private final QuantityAggregationTask.Statistic statistic = new QuantityAggregationTask.Statistic();

    /**
     * @param maxBufferSize 每个批次最大数量
     * @param listener      监听器
     */
    public AbstractAggregationTask(int maxBufferSize, AggregationTaskListener<T> listener) {
        this.maxBufferSize = maxBufferSize;
        this.listener = listener;
    }

    public abstract void add(T value);

    @Getter
    public static class Statistic {
        /**
         * 已刷新数量
         */
        private int flushSize;
        /**
         * 已接收数量
         */
        private int receiveSize;

        protected void incrementFlushSize(int size) {
            flushSize += size;
        }

        protected void incrementReceiveSize() {
            receiveSize += 1;
        }
    }
}