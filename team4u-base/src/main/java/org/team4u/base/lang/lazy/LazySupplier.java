package org.team4u.base.lang.lazy;


import cn.hutool.core.convert.Convert;
import cn.hutool.log.Log;
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

    private final Supplier<? extends T> supplier;

    private LazySupplier(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    /**
     * 创建懒加载提供者
     *
     * @param supplier 值提供者
     * @param <T>      值类型
     * @return 懒加载提供者
     */
    public static <T> LazySupplier<T> of(Supplier<? extends T> supplier) {
        return new LazySupplier<>(supplier);
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
                    log.info(lm.success().append("v", formatResultForLog(value)).toString());
                }
            }
        }

        return value;
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
}