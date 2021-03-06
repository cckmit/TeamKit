package org.team4u.base.lang;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.lang.func.Func0;

/**
 * 可缓存结果的无参数函数
 * <p>
 * 注意，结果为null不会进行缓存，请用其他对象代替null的含义
 *
 * @param <R> 返回值类型
 * @author jay.wu
 * @see org.team4u.base.lang.lazy.LazySupplier
 * @deprecated 使用LazySupplier代替
 */
public abstract class CacheableFunc0<R> implements Func0<R> {

    private final Delegate delegate;

    public CacheableFunc0() {
        this(CacheUtil.newLRUCache(1));
    }

    public CacheableFunc0(Cache<Class<?>, R> cache) {
        delegate = new Delegate(cache);
    }

    /**
     * 执行函数并缓存结果
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     */
    public R callWithCache() {
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

    /**
     * 删除缓存
     */
    public void removeCache() {
        delegate.removeCache(this.getClass());
    }

    public void reset() {
        delegate.reset();
    }

    private class Delegate extends CacheableFunc1<Class<?>, R> {

        public Delegate(Cache<Class<?>, R> cache) {
            super(cache);
        }

        @Override
        public R call(Class<?> parameter) throws Exception {
            return CacheableFunc0.this.call();
        }
    }
}