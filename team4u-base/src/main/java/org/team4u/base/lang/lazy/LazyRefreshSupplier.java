package org.team4u.base.lang.lazy;

import org.team4u.base.lang.LongTimeThread;

import java.util.function.Supplier;

/**
 * 懒加载刷新提供者
 *
 * @author jay.wu
 */
public class LazyRefreshSupplier<T> extends LongTimeThread implements Supplier<T> {

    private T value;

    private final long refreshIntervalMillis;
    private final Supplier<? extends T> supplier;

    public static <T> LazyRefreshSupplier<T> of(long refreshIntervalMillis, Supplier<? extends T> supplier) {
        return new LazyRefreshSupplier<>(refreshIntervalMillis, supplier);
    }

    public LazyRefreshSupplier(long refreshIntervalMillis, Supplier<? extends T> supplier) {
        this.refreshIntervalMillis = refreshIntervalMillis;
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (value != null) {
            return value;
        }

        synchronized (this) {
            if (value == null) {
                onRun();
                start();
            }
        }

        return value;
    }

    public T value() {
        return value;
    }

    @Override
    protected void onRun() {
        T newValue = supplier.get();

        if (newValue == null) {
            throw new IllegalStateException("Lazy value can not be null!");
        }

        value = newValue;
    }

    @Override
    protected Number runIntervalMillis() {
        return refreshIntervalMillis;
    }

    @Override
    protected boolean isSleepBeforeRun() {
        return true;
    }
}