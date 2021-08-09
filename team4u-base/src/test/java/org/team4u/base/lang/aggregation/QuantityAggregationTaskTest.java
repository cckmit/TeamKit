package org.team4u.base.lang.aggregation;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QuantityAggregationTaskTest {

    @Test
    public void add() {
        AtomicInteger count = new AtomicInteger();
        AtomicInteger size = new AtomicInteger();

        QuantityAggregationTask<Integer> task = new QuantityAggregationTask<>(
                2, new AggregationTaskListener<Integer>() {
            @Override
            public void onReceive(AbstractAggregationTask<Integer> task, Integer value) {
                count.incrementAndGet();
            }

            @Override
            public void onFlush(AbstractAggregationTask<Integer> task, List<Integer> values) {
                count.incrementAndGet();
                size.addAndGet(values.size());
            }
        });

        task.add(1);
        Assert.assertEquals(1, count.get());
        Assert.assertEquals(0, size.get());
        Assert.assertEquals(1, task.getStatistic().getReceiveSize());
        Assert.assertEquals(0, task.getStatistic().getFlushSize());

        // 自动刷新
        task.add(1);
        Assert.assertEquals(3, count.get());
        Assert.assertEquals(2, size.get());
        Assert.assertEquals(2, task.getStatistic().getReceiveSize());
        Assert.assertEquals(2, task.getStatistic().getFlushSize());

        // 空
        task.flush();
        Assert.assertEquals(4, count.get());
        Assert.assertEquals(2, size.get());
        Assert.assertEquals(2, task.getStatistic().getReceiveSize());
        Assert.assertEquals(2, task.getStatistic().getFlushSize());
    }
}