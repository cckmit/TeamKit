package org.team4u.kit.core.test.util;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.action.Callback;
import org.team4u.kit.core.lang.BufferTask;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Jay Wu
 */
public class BufferTaskTest {

    @Test
    public void start() {
        final AtomicInteger count = new AtomicInteger();
        BufferTask<String> task = new BufferTask<String>()
                .setMaxBufferSize(1)
                .setTimeout(1, TimeUnit.SECONDS)
                .setWorker(new Callback<List<String>>() {
                    @Override
                    public void invoke(List<String> obj) {
                        count.incrementAndGet();
                    }
                })
                .start();
        task.put("1");
        task.put("2");
        task.put("3");
        task.close();
        task.awaitTermination(2, TimeUnit.SECONDS);
        Assert.assertTrue(count.get() == 3);
    }
}