package org.team4u.base.lang;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TimedAggregationTaskTest {

    @Test
    public void start() {
        AtomicInteger count = new AtomicInteger();
        AtomicInteger size = new AtomicInteger();

        TimedAggregationTask<Integer> task = new TimedAggregationTask<>(
                2, 100, new TimedAggregationTask.Listener<Integer>() {
            @Override
            public void onReceive(TimedAggregationTask<Integer> task, Integer value) {
                count.incrementAndGet();
            }

            @Override
            public void onFlush(TimedAggregationTask<Integer> task, List<Integer> values) {
                count.incrementAndGet();
                size.addAndGet(values.size());
            }
        });

        task.start();

        task.add(1);
        task.add(1);

        ThreadUtil.sleep(50);
        Assert.assertEquals(3, count.get());
        Assert.assertEquals(2, size.get());

        // 超时
        ThreadUtil.sleep(101);
        Assert.assertEquals(4, count.get());
        Assert.assertEquals(2, size.get());
    }
}