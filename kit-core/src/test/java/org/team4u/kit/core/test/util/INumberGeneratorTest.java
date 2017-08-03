package org.team4u.kit.core.test.util;

import junit.framework.Assert;
import org.junit.Test;
import org.team4u.kit.core.action.Function;
import org.team4u.kit.core.id.DistributedLongGenerator;
import org.team4u.kit.core.id.MemoryMillisLongGenerator;
import org.team4u.kit.core.util.ThreadExUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class INumberGeneratorTest {

    @Test
    public void nextMemoryMillisLongGenerator() throws InterruptedException {
        final MemoryMillisLongGenerator generator = new MemoryMillisLongGenerator(1);
        Long last = null;
        for (int i = 0; i < 11; i++) {
            last = generator.next();
        }

        String lastString = last.toString();
        Assert.assertEquals("0", lastString.substring(lastString.length() - 1));

        Runnable next = new Runnable() {
            @Override
            public void run() {
                generator.next();
            }
        };

        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000000; i++) {
            executor.execute(next);
        }

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }

    @Test
    public void nextDistributedLongGenerator() throws InterruptedException {
        final Set<Long> result = new ConcurrentSkipListSet<Long>();
        final int MAX_WORKER_ID = 64;
        final int MAX_WORKER_OUTPUT_QUANTITY = 100000;

        Function<Integer, Thread> worker = new Function<Integer, Thread>() {

            @Override
            public Thread invoke(final Integer index) {
                return ThreadExUtil.startThread(new Runnable() {
                    @Override
                    public void run() {
                        DistributedLongGenerator generator = new DistributedLongGenerator.Builder(index)
                                .setMaxSequence(4095)
                                .setMaxWorkerId(MAX_WORKER_ID)
                                .build();

                        for (int i = 0; i < MAX_WORKER_OUTPUT_QUANTITY; i++) {
                            result.add(generator.next());
                        }
                    }
                });
            }
        };

        List<Thread> threads = new ArrayList<Thread>();

        for (int i = 0; i < MAX_WORKER_ID; i++) {
            threads.add(worker.invoke(i));
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Assert.assertEquals(MAX_WORKER_ID * MAX_WORKER_OUTPUT_QUANTITY, result.size());
    }

    @Test
    public void getWorkIdOfSeq() {
        DistributedLongGenerator generator = new DistributedLongGenerator.Builder(62)
                .setMaxSequence(4095)
                .setMaxWorkerId(64)
                .build();
        Long id = generator.next();
        Assert.assertEquals(62, generator.getWorkIdOfSeq(id).intValue());
    }
}