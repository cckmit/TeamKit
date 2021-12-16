package org.team4u.base.lang.lazy;

import cn.hutool.cache.CacheUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.TestUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class LazyFunctionTest {

    @Test
    public void concurrent() {
        checkConcurrent(new Date());
        checkConcurrent(this.getClass());
        checkConcurrent(TestUtil.TEST);
    }

    private void checkConcurrent(Object key) {
        AtomicInteger counter = new AtomicInteger();

        LazyFunction<Object, Integer> f = LazyFunction.of(
                LazyFunction.Config.builder().cache(CacheUtil.newLRUCache(1)).build(),
                integer -> counter.incrementAndGet()
        );

        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            futures.add(ThreadUtil.execAsync(() -> {
                f.apply(key);
            }));
        }

        futures.forEach(it -> {
            try {
                it.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Assert.assertEquals(1, f.apply(key).intValue());
    }

    @Test
    public void apply() {
        AtomicInteger i = new AtomicInteger();

        LazyFunction<Integer, Integer> f = LazyFunction.of(
                LazyFunction.Config.builder().cache(CacheUtil.newLRUCache(1)).build(),
                integer -> i.getAndIncrement()
        );

        Assert.assertEquals(0, f.apply(0).intValue());
        Assert.assertEquals(0, f.apply(0).intValue());

        f.reset();
        Assert.assertEquals(1, f.apply(0).intValue());
    }

    @Test
    public void notAllowNullValue() {
        LazyFunction<Integer, Integer> f = LazyFunction.of(t -> null);

        try {
            f.apply(1);
            Assert.fail();
        } catch (NullValueException e) {
            // pass
        }
    }

    @Test
    public void allowNullValue() {
        AtomicInteger i = new AtomicInteger(0);
        LazyFunction<Integer, Integer> f = LazyFunction.of(
                LazyFunction.Config.builder()
                        .isAllowReturnNullValue(true)
                        .build(),
                t -> {
                    i.incrementAndGet();
                    return null;
                });

        Assert.assertNull(f.apply(1));
        Assert.assertNull(f.apply(1));
        Assert.assertEquals(2, i.get());
    }

    @Test
    public void map() {
        AtomicInteger i = new AtomicInteger(1);
        LazyFunction<Integer, String> f = LazyFunction.<Integer, Integer>of(it -> it + i.getAndIncrement())
                .map(it -> it + i.getAndIncrement())
                .map(String::valueOf);

        Assert.assertEquals("4", f.apply(1));
    }

    @Test
    public void flatMap() {
        LazyFunction<Integer, String> f1 = LazyFunction.of(String::valueOf);
        LazyFunction<Integer, String> f2 = f1.flatMap(x ->
                LazyFunction.<Integer, String>of(String::valueOf)
                        .map(y -> x + y)
        );

        Assert.assertEquals("11", f2.apply(1));
    }

    @Test
    public void nullValue() {
        try {
            LazyFunction.of(it -> null);
        } catch (IllegalStateException e) {
            Assert.assertEquals("Lazy value can not be null!", e.getMessage());
        }
    }
}