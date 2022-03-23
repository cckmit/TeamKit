package org.team4u.base.lang.lazy;


import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.CacheObj;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.caller.CallerUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.base.log.LogMessage;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        config.setName(ObjectUtil.defaultIfNull(config.getName(), CallerUtil.getCallerCaller().getSimpleName()));
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

    /**
     * 根据Key获取或者创建值
     *
     * <p>
     * - 若值不存在则创建
     * <p>
     * - 相同key仅创建一次
     *
     * @param key 键
     * @return 值
     */
    @Override
    public R apply(T key) {
        Object cacheKey = cacheKey(key);

        R result = valueOfCacheKey(cacheKey);
        if (result != null) {
            return result;
        }

        synchronized (lockOfKey(cacheKey)) {
            result = valueOfCacheKey(cacheKey);
            if (result != null) {
                return result;
            }

            return applyNewValue(cacheKey, key);
        }
    }

    /**
     * 直接获取值
     * <p>
     * 该方法不会创建值
     *
     * @param key 键
     * @return 仅返回已存在的值，不存在则返回null
     */
    public R value(T key) {
        return valueOfCacheKey(cacheKey(key));
    }

    private Object lockOfKey(Object cacheKey) {
        if (cacheKey == null) {
            return this;
        }

        if (cacheKey instanceof String) {
            return ((String) cacheKey).intern();
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    private Object cacheKey(T key) {
        return config.getKeyFunc().apply(key);
    }

    @SuppressWarnings("unchecked")
    private R applyNewValue(Object cacheKey, T inputKey) {
        LogMessage lm = LogMessage.create(config.getName(), "create")
                .append("parameter", config.getParameterFormatter().format(log, inputKey));

        R result = valueFunc.apply(inputKey);

        checkValue(lm, result);

        saveValue(cacheKey, result);

        if (config.isEnabledLog() && log.isInfoEnabled()) {
            log.info(lm.success()
                    .append("value", config.getResultFormatter().format(log, result))
                    .toString());
        }

        return result;
    }

    /**
     * 根据最终的缓存key获取值
     *
     * @param cacheKey 最终的缓存key
     * @return 值
     */
    @SuppressWarnings("unchecked")
    private R valueOfCacheKey(Object cacheKey) {
        return (R) config.getCache().get(cacheKey);
    }

    @SuppressWarnings("unchecked")
    private void saveValue(Object cacheKey, R result) {
        if (result == null) {
            return;
        }

        config.getCache().put(cacheKey, result);
    }

    private void checkValue(LogMessage lm, R result) {
        if (result != null) {
            return;
        }

        if (config.isAllowReturnNullValue()) {
            return;
        }

        IllegalStateException e = new NullValueException("Lazy value can not be null!");
        log.error(lm.fail(e.getMessage()).toString());
        throw e;
    }

    /**
     * 值数量
     */
    public int size() {
        return config.getCache().size();
    }

    /**
     * 重置缓存
     */
    public void reset() {
        config.getCache().clear();
    }


    /**
     * 删除值
     *
     * @param key 目标key
     */
    public void remove(T key) {
        removeByCacheKey(cacheKey(key));
    }

    /**
     * 根据最终的缓存key删除值
     *
     * @param cacheKey 经过keyFunc转换后的最终key
     */
    @SuppressWarnings("unchecked")
    public void removeByCacheKey(Object cacheKey) {
        LogMessage lm = LogMessage.create(config.getName(), "remove");

        config.getCache().remove(cacheKey);

        log.info(lm.append("key", cacheKey).append("size", size()).success().toString());
    }

    /**
     * 获取缓存键值对集合
     * <p>
     * 注意：key为经过keyFunc转换后的最终key
     */
    @SuppressWarnings("unchecked")
    public List<CacheObj<?, R>> keyAndValues() {
        return CollUtil.newArrayList(config.getCache().cacheObjIterator());
    }

    /**
     * 获取值集合
     */
    public List<R> values() {
        return keyAndValues()
                .stream()
                .map(CacheObj::getValue)
                .collect(Collectors.toList());
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
         * 是否允许返回null值
         * <p>
         * 若允许null值，则无法区分是否真正存在缓存，可能导致每次都会调用valueFunc进行计算
         */
        @Builder.Default
        private boolean isAllowReturnNullValue = false;
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
         * 是否开启日志
         * 默认为开启，仅打印info级别
         */
        @Builder.Default
        private boolean isEnabledLog = true;
        /**
         * 请求值日志格式化器
         */
        @SuppressWarnings("rawtypes")
        @Builder.Default
        private LazyValueFormatter parameterFormatter = new DefaultLazyValueFormatter();
        /**
         * 返回值日志格式化器
         */
        @SuppressWarnings("rawtypes")
        @Builder.Default
        private LazyValueFormatter resultFormatter = new DefaultLazyValueFormatter();
    }
}