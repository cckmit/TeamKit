package org.team4u.base.lang.aggregation;


import java.util.ArrayList;
import java.util.List;

/**
 * 数量限制的聚合任务
 * <p>
 * 当满足指定数量后，将批量刷新
 *
 * @author jay.wu
 */
public class QuantityAggregationTask<T> extends AbstractAggregationTask<T> {

    private final List<T> buffer = new ArrayList<>();

    /**
     * @param maxBufferSize 每个批次最大数量
     * @param listener      监听器
     */
    public QuantityAggregationTask(int maxBufferSize, AggregationTaskListener<T> listener) {
        super(maxBufferSize, listener);
    }

    @Override
    public void add(T value) {
        receive(value);

        if (buffer.size() == getMaxBufferSize()) {
            flush();
        }
    }

    public void flush() {
        getListener().onFlush(this, buffer);

        getStatistic().incrementFlushSize(buffer.size());

        buffer.clear();
    }

    private void receive(T value) {
        buffer.add(value);

        getListener().onReceive(this, value);

        getStatistic().incrementReceiveSize();
    }

    @Override
    public void close() {
        flush();
    }
}