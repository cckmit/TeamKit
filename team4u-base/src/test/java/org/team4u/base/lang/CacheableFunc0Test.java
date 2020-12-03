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

    @Test
    public void callWithError() {
        CacheableFunc0<Integer> f = errorWorker(null);
        checkError(f, 0);
        checkError(f, 1);
    }

    @Test
    public void callWithCacheError() {
        CacheableFunc0<Integer> f = errorWorker(RuntimeException.class);
        checkError(f, 0);
        checkError(f, 0);
    }

    private CacheableFunc0<Integer> errorWorker(Class<? extends Exception> errorType) {
        AtomicInteger i = new AtomicInteger();
        return new CacheableFunc0<Integer>(errorType == null ? null : new Class<?>[]{errorType}) {

            @Override
            public Integer call() {
                throw new RuntimeException(i.getAndIncrement() + "");
            }
        };
    }

    private void checkError(CacheableFunc0<Integer> func, int expected) {
        try {
            func.callWithCache();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(expected + "", e.getMessage());
        }
    }
}