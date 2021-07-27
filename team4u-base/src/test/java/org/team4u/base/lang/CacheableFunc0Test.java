package org.team4u.base.lang;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class CacheableFunc0Test {

    @Test
    public void callWithCache() {
        AtomicInteger i = new AtomicInteger();
        CacheableFunc0<Integer> f = new CacheableFunc0<Integer>() {

            @Override
            public Integer call() {
                return i.getAndIncrement();
            }
        };

        Assert.assertEquals(0, f.callWithCacheAndRuntimeException().intValue());
        Assert.assertEquals(1, f.callWithRuntimeException().intValue());
        Assert.assertEquals(0, f.callWithCacheAndRuntimeException().intValue());

        f.reset();
        Assert.assertEquals(2, f.callWithCacheAndRuntimeException().intValue());
    }
}