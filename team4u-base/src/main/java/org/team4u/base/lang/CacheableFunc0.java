package org.team4u.base.lang;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.func.Func0;

/**
 * 可缓存结果的无参数函数
 *
 * @param <R> 返回值类型
 * @author jay.wu
 */
public abstract class CacheableFunc0<R> implements Func0<R> {

    private final Delegate delegate;

    public CacheableFunc0() {
        this((Class<?>[]) null);
    }

    public CacheableFunc0(Class<?>[] cacheForExceptionClasses) {
        this(CacheUtil.newLRUCache(1), cacheForExceptionClasses);
    }

    public CacheableFunc0(Cache<Class<?>, R> cache) {
        this(cache, null);
    }

    public CacheableFunc0(Cache<Class<?>, R> cache, Class<?>[] cacheForExceptionClasses) {
        delegate = new Delegate(cache, cacheForExceptionClasses);
    }

    /**
     * 执行函数并缓存结果
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     * @throws Exception 自定义异常
     */
    public R callWithCache() throws Exception {
        return delegate.callWithCache(this.getClass());
    }

    @Override
    public R callWithRuntimeException() {
        return delegate.callWithRuntimeException(this.getClass());
    }

    /**
     * 执行函数并缓存结果，异常包装为RuntimeException
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     */
    public R callWithCacheAndRuntimeException() {
        return delegate.callWithCacheAndRuntimeException(this.getClass());
    }

    public void reset() {
        delegate.reset();
    }

    private class Delegate extends CacheableFunc1<Class<?>, R> {

        public Delegate(Cache<Class<?>, R> cache) {
            super(cache);
        }

        public Delegate(Cache<Class<?>, R> cache, Class<?>[] cacheForExceptionClasses) {
            super(cache, cacheForExceptionClasses);
        }

        @Override
        public R call(Class<?> parameter) throws Exception {
            return CacheableFunc0.this.call();
        }
    }
}