package org.team4u.base.lang.lazy;

import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.util.ObjectUtil;
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

    private T value;

    private final Config config;
    private final Supplier<T> supplier;

    public static <T> LazyRefreshSupplier<T> of(Supplier<T> supplier) {
        return new LazyRefreshSupplier<>(new Config(), supplier);
    }

    public static <T> LazyRefreshSupplier<T> of(LazyRefreshSupplier.Config config, Supplier<T> supplier) {
        return new LazyRefreshSupplier<>(config, supplier);
    }

    public LazyRefreshSupplier(Config config, Supplier<T> supplier) {
        this.config = config;
        this.supplier = supplier;

        config.setName(ObjectUtil.defaultIfNull(
                config.getName(),
                CallerUtil.getCallerCaller().getSimpleName()
        ));
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
        refresh();
    }

    public synchronized void refresh() {
        LogMessage lm = LogMessage.create(config.getName(), "refresh");

        T newValue = supplier.get();
        assertNotNull(lm, newValue);

        T oldValue = value;
        value = newValue;

        // 仅打印变化值
        logIfChanged(lm, oldValue, newValue);

        notifyRefreshListener(oldValue, newValue);
    }

    private void notifyRefreshListener(T oldValue, T newValue) {
        if (config.getRefreshListener() == null) {
            return;
        }

        //noinspection unchecked
        config.getRefreshListener().afterRefresh(oldValue, newValue);
    }

    private void assertNotNull(LogMessage lm, T value) {
        if (value != null) {
            return;
        }

        IllegalStateException e = new NullValueException("Lazy value can not be null!");
        log.error(lm.fail(e.getMessage()).toString());
        throw e;
    }

    private void logIfChanged(LogMessage lm, T oldValue, T newValue) {
        if (!log.isInfoEnabled()) {
            return;
        }

        if (!ObjectUtil.equal(oldValue, newValue)) {
            //noinspection unchecked
            log.info(lm.success()
                    .append("value", config.getValueFormatter().format(log, newValue))
                    .toString());
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
         * 名称
         */
        private String name;
        /**
         * 刷新间隔（毫秒）
         */
        @Builder.Default
        private long refreshIntervalMillis = 5000;
        /**
         * 返回值日志格式化器
         */
        @SuppressWarnings("rawtypes")
        @Builder.Default
        private LazyValueFormatter valueFormatter = new DefaultLazyValueFormatter();
        /**
         * 刷新监听器
         */
        @SuppressWarnings("rawtypes")
        private RefreshListener refreshListener;
    }

    /**
     * 刷新监听器
     *
     * @param <T> 值类似
     */
    public interface RefreshListener<T> {
        /**
         * 刷新后回调
         *
         * @param oldValue 旧值
         * @param newValue 新值
         */
        void afterRefresh(T oldValue, T newValue);
    }
}