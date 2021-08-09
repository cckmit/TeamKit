package org.team4u.base.lang.aggregation;

import java.util.List;

/**
 * 聚合任务监听器
 *
 * @author jay.wu
 */
public interface AggregationTaskListener<T> {

    /**
     * 新增数据时回调
     *
     * @param value 新增数据
     */
    void onReceive(AbstractAggregationTask<T> task, T value);

    /**
     * 需要刷新数据时回调
     *
     * @param values 需要刷新的数据集合，若为空则表示超时仍然无数据
     */
    void onFlush(AbstractAggregationTask<T> task, List<T> values);
}