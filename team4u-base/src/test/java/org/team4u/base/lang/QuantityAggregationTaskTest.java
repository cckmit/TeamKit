package org.team4u.base.lang;

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
                2, new QuantityAggregationTask.Listener<Integer>() {
            @Override
            public void onReceive(Integer value) {
                count.incrementAndGet();
            }

            @Override
            public void onFlush(List<Integer> values) {
                count.incrementAndGet();
                size.addAndGet(values.size());
            }
        });

        task.add(1);
        Assert.assertEquals(1, count.get());
        Assert.assertEquals(0, size.get());

        // 自动刷新
        task.add(1);
        Assert.assertEquals(3, count.get());
        Assert.assertEquals(2, size.get());

        // 空
        task.flush();
        Assert.assertEquals(4, count.get());
        Assert.assertEquals(2, size.get());
    }
}