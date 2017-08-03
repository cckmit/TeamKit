package org.team4u.kit.core.test.lang;

import com.xiaoleilu.hutool.util.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.lang.LongTimeThread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jay Wu
 */
public class LongTimeThreadTest {

    @Test
    public void startAndClosed() throws InterruptedException {
        final AtomicInteger counter = new AtomicInteger();
        LongTimeThread t = new LongTimeThread() {

            @Override
            protected void onRun() {
                counter.incrementAndGet();
                ThreadUtil.safeSleep(1000);
            }
        };

        t.start();
        ThreadUtil.safeSleep(1100);
        t.close();
        t.waitForClosed();
        Assert.assertEquals(2, counter.get());
    }
}