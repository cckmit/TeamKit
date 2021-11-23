package org.team4u.base.lang.lazy;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessage;

import java.util.function.Supplier;

/**
 * 懒加载刷新提供者
 *
 * @author jay.wu
 */
public class LazyRefreshSupplier<T> extends LongTimeThread implements Supplier<T> {

    private static final Log log = Log.get();

    private T value;

    private final Supplier<T> supplier;

    private final Config config;

    public static <T> LazyRefreshSupplier<T> of(Supplier<T> supplier) {
        return new LazyRefreshSupplier<>(new Config(), supplier);
    }

    public static <T> LazyRefreshSupplier<T> of(LazyRefreshSupplier.Config config, Supplier<T> supplier) {
        return new LazyRefreshSupplier<>(config, supplier);
    }

    public LazyRefreshSupplier(Config config, Supplier<T> supplier) {
        this.config = config;
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

        T newValue = supplier.get();

        if (newValue == null) {
            IllegalStateException e = new IllegalStateException("Lazy value can not be null!");
            log.error(lm.fail(e.getMessage()).toString());
            throw e;
        }

        T oldValue = value;
        value = newValue;

        // 仅打印变化值
        if (log.isInfoEnabled()) {
            if (!ObjectUtil.equal(oldValue, newValue)) {
                log.info(lm.success()
                        .append("value", config.getValueFormatter().format(log, newValue))
                        .toString());
            }
        }

        if (config.getRefreshListener() != null) {
            //noinspection unchecked
            config.getRefreshListener().afterRefresh(oldValue, newValue);
        }
    }

    @Override
    protected Number runIntervalMillis() {
        return config.getRefreshIntervalMillis();
    }

    @Override
    protected boolean isSleepBeforeRun() {
        return true;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {

        /**
         * 刷新间隔（毫秒）
         */
        @Builder.Default
        long refreshIntervalMillis = 5000;

        /**
         * 返回值日志格式化器
         */
        @Builder.Default
        LazyValueFormatter valueFormatter = new LazyValueFormatter();

        /**
         * 刷新监听器
         */
        @SuppressWarnings("rawtypes")
        RefreshListener refreshListener;
    }

    /**
     * 刷新监听器
     *
     * @param <T> 值类似
     */
    interface RefreshListener<T> {
        /**
         * 刷新后回调
         *
         * @param oldValue 旧值
         * @param newValue 新值
         */
        void afterRefresh(T oldValue, T newValue);
    }
}