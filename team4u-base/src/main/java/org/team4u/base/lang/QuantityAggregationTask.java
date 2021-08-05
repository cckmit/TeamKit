package org.team4u.base.lang;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数量限制的聚合任务
 * <p>
 * 当满足指定数量后，将批量刷新
 *
 * @author jay.wu
 */
public class QuantityAggregationTask<T> {

    private final List<T> buffer = new ArrayList<>();

    @Getter
    private final int maxBufferSize;
    @Getter
    private final Listener<T> listener;

    /**
     * @param maxBufferSize 每个批次最大数量
     * @param listener      监听器
     */
    public QuantityAggregationTask(int maxBufferSize, Listener<T> listener) {
        this.maxBufferSize = maxBufferSize;
        this.listener = listener;
    }

    public void add(T value) {
        receive(value);

        if (buffer.size() == maxBufferSize) {
            flush();
        }
    }

    public void flush() {
        listener.onFlush(buffer);
        buffer.clear();
    }

    private void receive(T value) {
        buffer.add(value);
        listener.onReceive(value);
    }

    public interface Listener<T> {
        /**
         * 新增数据时回调
         *
         * @param value 新增的数据
         */
        void onReceive(T value);

        /**
         * 需要刷新数据时回调
         *
         * @param values 需要刷新的数据集合，若为空则表示最后一批
         */
        void onFlush(List<T> values);
    }
}