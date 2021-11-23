package org.team4u.base.lang.lazy;

import cn.hutool.cache.CacheUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class LazyFunctionTest {

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