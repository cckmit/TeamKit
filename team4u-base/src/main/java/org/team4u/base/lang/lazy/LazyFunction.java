package org.team4u.base.lang.lazy;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    private final Config config;
    private final Function<T, R> valueFunc;

    public LazyFunction(Config config, Function<T, R> valueFunc) {
        this.config = config;
        this.valueFunc = valueFunc;

        config.setName(ObjectUtil.defaultIfNull(
                config.getName(),
                CallerUtil.getCallerCaller().getSimpleName()
        ));
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
        return new LazyFunction<>(new Config(), valueFunc);
    }

    /**
     * 创建懒加载函数
     *
     * @param config    配置
     * @param valueFunc 值函数
     * @param <T>       入参类型
     * @param <R>       返回类型
     * @return 懒加载函数
     */
    public static <T, R> LazyFunction<T, R> of(Config config, Function<T, R> valueFunc) {
        return new LazyFunction<>(config, valueFunc);
    }

    @SuppressWarnings("unchecked")
    @Override
    public R apply(T t) {
        Object cacheKey = config.getKeyFunc().apply(t);

        R result = (R) config.getCache().get(cacheKey);
        if (result != null) {
            return result;
        }

        synchronized (this) {
            result = (R) config.getCache().get(cacheKey);
            if (result != null) {
                return result;
            }

            LogMessage lm = LogMessage.create(config.getName(), "create")
                    .append("parameter", config.getParameterFormatter().format(log, t));

            if (!ObjectUtil.equals(cacheKey, t)) {
                lm.append("parameter", cacheKey);
            }

            result = valueFunc.apply(t);
            if (result == null) {
                IllegalStateException e = new IllegalStateException("Lazy value can not be null!");
                log.error(lm.fail(e.getMessage()).toString());
                throw e;
            }
            config.getCache().put(cacheKey, result);

            if (log.isInfoEnabled()) {
                log.info(lm.success().append("value", config.getResultFormatter().format(log, result)).toString());
            }
        }

        return result;
    }

    /**
     * 重置缓存
     */
    public void reset() {
        config.getCache().clear();
    }

    /**
     * 删除缓存
     *
     * @param keyFunc 返回需要删除的key，若key = null则不删除
     */
    @SuppressWarnings("unchecked")
    public int remove(Function<R, T> keyFunc) {
        int count = 0;
        for (Object value : config.getCache()) {
            Object key = keyFunc.apply((R) value);
            if (key == null) {
                continue;
            }

            config.getCache().remove(config.keyFunc.apply(key));
            count++;
        }

        return count;
    }

    public <R2> LazyFunction<T, R2> map(Function<R, R2> function) {
        return LazyFunction.of(config, t -> function.apply(apply(t)));
    }

    public <R2> LazyFunction<T, R2> flatMap(Function<R, LazyFunction<T, R2>> function) {
        return LazyFunction.of(config, t -> {
            R r = apply(t);
            return function.apply(r).apply(t);
        });
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
         * 缓存的执行结果
         */
        @Builder.Default
        @SuppressWarnings("rawtypes")
        private Cache cache = CacheUtil.newLRUCache(1000);
        /**
         * 缓存key函数
         */
        @Builder.Default
        @SuppressWarnings("rawtypes")
        private Function keyFunc = t -> t;
        /**
         * 请求值日志格式化器
         */
        @Builder.Default
        private LazyValueFormatter parameterFormatter = new LazyValueFormatter();
        /**
         * 返回值日志格式化器
         */
        @Builder.Default
        private LazyValueFormatter resultFormatter = new LazyValueFormatter();
    }
}