package org.team4u.base.lang;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.lang.lazy.LazySupplier;

import java.util.concurrent.atomic.AtomicInteger;

public class LazySupplierTest {

    @Test
    public void get() {
        AtomicInteger i = new AtomicInteger();
        LazySupplier<Integer> f = LazySupplier.of(i::getAndIncrement);

        Assert.assertEquals(0, f.get().intValue());
        Assert.assertEquals(0, f.get().intValue());

        f.reset();
        Assert.assertEquals(1, f.get().intValue());
    }

    @Test
    public void map() {
        AtomicInteger i = new AtomicInteger(1);
        LazySupplier<Integer> lazy = LazySupplier.of(i::getAndIncrement)
                .map((it) -> it + i.getAndIncrement())
                .map((it) -> it + i.getAndIncrement());

        Assert.assertEquals(6, lazy.get().intValue());
        Assert.assertEquals(6, lazy.get().intValue());
    }

    @Test
    public void flatMap() {
        AtomicInteger i = new AtomicInteger(1);
        LazySupplier<Integer> lazy = LazySupplier.of(i::getAndIncrement)
                .flatMap((a) -> LazySupplier.of(i::getAndIncrement).map((b) -> a + b));

        Assert.assertEquals(3, lazy.get().intValue());
        Assert.assertEquals(3, lazy.get().intValue());
    }

    @Test
    public void nullValue() {
        try {
            LazySupplier.of(() -> null).get();
            Assert.fail();
        } catch (IllegalStateException e) {
            Assert.assertEquals("Lazy value can not be null!", e.getMessage());
        }
    }
}