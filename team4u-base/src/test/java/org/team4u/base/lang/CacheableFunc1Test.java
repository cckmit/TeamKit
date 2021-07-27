package org.team4u.base.lang;

import cn.hutool.cache.CacheUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class CacheableFunc1Test {

    @Test
    public void callWithCache() {
        AtomicInteger i = new AtomicInteger();
        CacheableFunc1<Integer, Integer> f = new CacheableFunc1<Integer, Integer>(CacheUtil.newLRUCache(10)) {

            @Override
            public Integer call(Integer p) {
                return i.getAndIncrement();
            }
        };

        Assert.assertEquals(0, f.callWithCacheAndRuntimeException(0).intValue());
        Assert.assertEquals(1, f.callWithRuntimeException(0).intValue());
        Assert.assertEquals(0, f.callWithCacheAndRuntimeException(0).intValue());

        f.reset();
        Assert.assertEquals(2, f.callWithCacheAndRuntimeException(0).intValue());
    }
}