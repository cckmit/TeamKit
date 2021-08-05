package org.team4u.base.lang;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AggregationTaskTest {

    @Test
    public void start() {
        AtomicInteger count = new AtomicInteger();
        AggregationTask<Integer> task = new AggregationTask<>(
                1, 100, new AggregationTask.Listener<Integer>() {

            @Override
            public void onEmpty(AggregationTask<Integer> task) {
                count.incrementAndGet();
            }

            @Override
            public void onAdd(AggregationTask<Integer> task, Integer value) {
                count.incrementAndGet();
            }

            @Override
            public void onFull(AggregationTask<Integer> task, List<Integer> values) {
                count.incrementAndGet();
            }
        });

        task.start();

        task.add(1);

        ThreadUtil.sleep(200);
        Assert.assertEquals(3, count.get());
    }
}