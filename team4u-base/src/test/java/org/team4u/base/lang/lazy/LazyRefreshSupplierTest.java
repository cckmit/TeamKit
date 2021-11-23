package org.team4u.base.lang.lazy;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class LazyRefreshSupplierTest {

    @Test
    public void get() {
        AtomicInteger i = new AtomicInteger();
        LazyRefreshSupplier<Integer> x = LazyRefreshSupplier.of(
                LazyRefreshSupplier.Config.builder()
                        .refreshIntervalMillis(50L)
                        .build(),
                i::incrementAndGet
        );
        Assert.assertEquals(1, x.get().intValue());
        Assert.assertEquals(1, x.get().intValue());

        ThreadUtil.sleep(70);
        Assert.assertEquals(2, x.get().intValue());
    }
}