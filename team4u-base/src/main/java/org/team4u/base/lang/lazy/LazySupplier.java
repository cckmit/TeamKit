package org.team4u.base.lang.lazy;


import cn.hutool.log.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.base.log.LogMessage;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 懒加载提供者
 * <p>
 * - 自动缓存结果
 * - 结果为null将抛出异常
 *
 * @param <T> 返回值类型
 * @author jay.wu
 */
public class LazySupplier<T> implements Supplier<T> {

    private final Log log = Log.get();

    private T value;

    private final Config config;
    private final Supplier<? extends T> supplier;

    public LazySupplier(Config config, Supplier<T> supplier) {
        this.config = config;
        this.supplier = supplier;
    }

    /**
     * 创建懒加载提供者
     *
     * @param supplier 值提供者
     * @param <T>      值类型
     * @return 懒加载提供者
     */
    public static <T> LazySupplier<T> of(Supplier<T> supplier) {
        return new LazySupplier<>(new Config(), supplier);
    }

    /**
     * 创建懒加载提供者
     *
     * @param supplier 值提供者
     * @param config   配置
     * @param <T>      值类型
     * @return 懒加载提供者
     */
    public static <T> LazySupplier<T> of(Config config, Supplier<T> supplier) {
        return new LazySupplier<>(config, supplier);
    }

    public T get() {
        if (value != null) {
            return value;
        }

        synchronized (this) {
            if (value == null) {
                LogMessage lm = LogMessage.create(this.getClass().getName(), "get")
                        .append("supplier", supplier.getClass().getName());

                T newValue = supplier.get();
                if (newValue == null) {
                    IllegalStateException e = new IllegalStateException("Lazy value can not be null!");
                    log.error(lm.fail(e.getMessage()).toString());
                    throw e;
                }
                value = newValue;

                if (log.isInfoEnabled()) {
                    log.info(lm.success()
                            .append("v", config.getValueFormatter().format(log, value))
                            .toString());
                }
            }
        }

        return value;
    }

    public <S> LazySupplier<S> map(Function<? super T, ? extends S> function) {
        return LazySupplier.of(() -> function.apply(get()));
    }

    public <S> LazySupplier<S> flatMap(Function<? super T, LazySupplier<? extends S>> function) {
        return LazySupplier.of(() -> function.apply(get()).get());
    }

    /**
     * 重置缓存
     */
    public void reset() {
        value = null;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {
        /**
         * 返回值日志格式化器
         */
        @Builder.Default
        private LazyValueFormatter valueFormatter = new LazyValueFormatter();
    }
}