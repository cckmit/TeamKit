package org.team4u.base.lang.lazy;

import org.team4u.base.lang.LongTimeThread;

import java.util.function.Supplier;

/**
 * 自动刷新的提供者
 *
 * @author jay.wu
 */
public class AutoRefreshSupplier<T> extends LongTimeThread implements Supplier<T> {

    private T value;

    private final long refreshIntervalMillis;
    private final Supplier<? extends T> supplier;

    public static <T> AutoRefreshSupplier<T> of(long refreshIntervalMillis, Supplier<? extends T> supplier) {
        return new AutoRefreshSupplier<>(refreshIntervalMillis, supplier);
    }

    public AutoRefreshSupplier(long refreshIntervalMillis, Supplier<? extends T> supplier) {
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
            throw new IllegalStateException("AutoRefreshSupplier value can not be null!");
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