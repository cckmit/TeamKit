package org.team4u.base.lang.lazy;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;

import java.util.function.Function;

/**
 * 懒加载函数
 * <p>
 * - 自动缓存结果
 * - 结果为null将抛出异常
 *
 * @param <T> 入参类型
 * @param <R> 返回类型
 * @author jay.wu
 */
public class LazyFunction<T, R> implements Function<T, R> {

    private final Log log = Log.get();

    /**
     * 缓存的执行结果
     */
    private final Cache<Object, R> cache;

    private final Function<T, ?> keyFunc;
    private final Function<T, R> valueFunc;

    public LazyFunction(Function<T, R> valueFunc) {
        this(valueFunc, t -> t);
    }

    public LazyFunction(Function<T, R> valueFunc, Function<T, ?> keyFunc) {
        this(CacheUtil.newLRUCache(1000), valueFunc, keyFunc);
    }

    public LazyFunction(Cache<?, ?> cache, Function<T, R> valueFunc) {
        this(cache, valueFunc, t -> t);
    }

    @SuppressWarnings("unchecked")
    public LazyFunction(Cache<?, ?> cache, Function<T, R> valueFunc, Function<T, ?> keyFunc) {
        this.cache = (Cache<Object, R>) cache;
        this.valueFunc = valueFunc;
        this.keyFunc = keyFunc;
    }

    /**
     * 创建懒加载函数
     *
     * @param valueFunc 创建值的函数
     * @param <T>       入参类型
     * @param <R>       返回类型
     * @return 懒加载函数
     */
    public static <T, R> LazyFunction<T, R> of(Function<T, R> valueFunc) {
        return new LazyFunction<>(valueFunc);
    }

    /**
     * 创建懒加载函数
     *
     * @param valueFunc 值函数
     * @param keyFunc   键函数，用于自定义键值，用于定位缓存值
     * @param <T>       入参类型
     * @param <R>       返回类型
     * @return 懒加载函数
     */
    public static <T, R> LazyFunction<T, R> of(Function<T, R> valueFunc, Function<T, ?> keyFunc) {
        return new LazyFunction<>(valueFunc, keyFunc);
    }

    /**
     * 创建懒加载函数
     *
     * @param cache     缓存
     * @param valueFunc 值函数
     * @param <T>       入参类型
     * @param <R>       返回类型
     * @return 懒加载函数
     */
    public static <T, R> LazyFunction<T, R> of(Cache<?, ?> cache, Function<T, R> valueFunc) {
        return new LazyFunction<>(cache, valueFunc);
    }

    /**
     * 创建懒加载函数
     *
     * @param cache     缓存
     * @param valueFunc 值函数
     * @param keyFunc   键函数，用于自定义键值，用于定位缓存值
     * @param <T>       入参类型
     * @param <R>       返回类型
     * @return 懒加载函数
     */
    public static <T, R> LazyFunction<T, R> of(Cache<?, ?> cache, Function<T, R> valueFunc, Function<T, ?> keyFunc) {
        return new LazyFunction<>(cache, valueFunc, keyFunc);
    }

    @Override
    public R apply(T t) {
        Object cacheKey = keyFunc.apply(t);

        R result = cache.get(cacheKey);
        if (result != null) {
            return result;
        }

        synchronized (this) {
            result = cache.get(cacheKey);
            if (result != null) {
                return result;
            }

            LogMessage lm = LogMessage.create(this.getClass().getName(), "apply")
                    .append("t", formatParameterForLog(t))
                    .append("valueFunc", valueFunc.getClass().getName());

            if (!ObjectUtil.equals(cacheKey, t)) {
                lm.append("k", cacheKey);
            }

            result = valueFunc.apply(t);
            if (result == null) {
                IllegalStateException e = new IllegalStateException("Lazy value can not be null!");
                log.error(lm.fail(e.getMessage()).toString());
                throw e;
            }
            cache.put(cacheKey, result);

            if (log.isInfoEnabled()) {
                log.info(lm.success().append("value", formatResultForLog(result)).toString());
            }
        }

        return result;
    }

    protected String formatParameterForLog(T parameter) {
        if (parameter == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            return Convert.toStr(parameter);
        }

        return parameter.getClass().getSimpleName();
    }

    protected String formatResultForLog(R result) {
        if (result == null) {
            return null;
        }

        if (log.isDebugEnabled()) {
            return Convert.toStr(result);
        }

        return result.getClass().getSimpleName();
    }

    /**
     * 重置缓存
     */
    public void reset() {
        cache.clear();
    }

    public <R2> LazyFunction<T, R2> map(Function<R, R2> function) {
        return LazyFunction.of(cache, t -> function.apply(apply(t)));
    }

    public <R2> LazyFunction<T, R2> flatMap(Function<R, LazyFunction<T, R2>> function) {
        return LazyFunction.of(cache, t -> {
            R r = apply(t);
            return function.apply(r).apply(t);
        });
    }
}