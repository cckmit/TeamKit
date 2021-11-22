package org.team4u.base.lang.lazy;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessage;

import java.util.function.Supplier;

/**
 * 懒加载刷新提供者
 *
 * @author jay.wu
 */
@Slf4j
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
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "onRefresh");

        T oldValue = value;
        T newValue = supplier.get();

        if (newValue == null) {
            IllegalStateException e = new IllegalStateException("Lazy value can not be null!");
            log.error(lm.fail(e.getMessage()).toString());
            throw e;
        }

        value = newValue;

        if (log.isInfoEnabled()) {
            if (!ObjectUtil.equal(oldValue, newValue)) {
                log.info(lm.success().append("value", formatResultForLog(newValue)).toString());
            }
        }

        afterRefresh(oldValue, newValue);
    }

    /**
     * 刷新后回调
     *
     * @param oldValue 旧值
     * @param newValue 新值
     */
    protected void afterRefresh(T oldValue, T newValue) {
    }

    protected String formatResultForLog(T result) {
        if (result == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            return Convert.toStr(result);
        }

        return result.getClass().getSimpleName();
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