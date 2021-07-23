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

    @Test
    public void callWithError() {
        CacheableFunc1<Integer, Integer> f = errorWorker(null);
        checkError(f, 0);
        checkError(f, 1);
    }

    @Test
    public void callWithCacheError() {
        CacheableFunc1<Integer, Integer> f = errorWorker(RuntimeException.class);
        checkError(f, 0);
        checkError(f, 0);
    }

    private CacheableFunc1<Integer, Integer> errorWorker(Class<? extends Exception> errorType) {
        AtomicInteger i = new AtomicInteger();
        return new CacheableFunc1<Integer, Integer>(
                CacheUtil.newLRUCache(10),
                errorType == null ? null : new Class<?>[]{errorType}) {

            @Override
            public Integer call(Integer p) {
                throw new RuntimeException(i.getAndIncrement() + "");
            }
        };
    }

    private void checkError(CacheableFunc1<Integer, Integer> func, int expected) {
        try {
            func.callWithCache(expected);
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(expected + "", e.getMessage());
        }
    }
}