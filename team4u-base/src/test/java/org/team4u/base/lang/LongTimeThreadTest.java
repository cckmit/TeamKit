package org.team4u.base.lang;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author jay.wu
 */
public class LongTimeThreadTest {

    @Test
    public void startAndClosed() {
        final AtomicInteger counter = new AtomicInteger();
        LongTimeThread t = new LongTimeThread() {

            @Override
            protected void onRun() {
                counter.incrementAndGet();
            }

            @Override
            protected Number runIntervalMillis() {
                return 10;
            }
        };

        t.start();
        ThreadUtil.safeSleep(10);
        t.close();
        t.awaitTermination();
        Assert.assertEquals(2, counter.get());
    }
}