package org.team4u.base.lang;

import cn.hutool.cache.Cache;
import cn.hutool.core.lang.func.Func1;
import org.team4u.base.error.NestedException;

import java.util.Arrays;

/**
 * 可缓存结果的单参数函数
 * <p>
 * 注意：仅缓存最近一次的请求结果
 *
 * @param <P> 入参类型
 * @param <R> 返回值类型
 * @author jay.wu
 */
public abstract class CacheableFunc1<P, R> implements Func1<P, R> {

    private final Class<?>[] cacheForExceptionClasses;
    /**
     * 缓存的执行结果
     */
    private final Cache<P, R> cache;
    /**
     * 缓存的执行异常
     */
    private Exception e;

    public CacheableFunc1(Cache<P, R> cache) {
        this(cache, null);
    }

    /**
     * @param cacheForExceptionClasses 需要缓存的异常类型，为null时不缓存
     */
    public CacheableFunc1(Cache<P, R> cache, Class<?>[] cacheForExceptionClasses) {
        this.cache = cache;
        this.cacheForExceptionClasses = cacheForExceptionClasses;
    }

    /**
     * 执行函数并缓存结果
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     * @throws Exception 自定义异常
     */
    public R callWithCache(P parameter) throws Exception {
        if (e != null) {
            throw e;
        }

        // 入参相同，且已有结果，直接返回
        if (cache.containsKey(parameter)) {
            return cache.get(parameter);
        }

        synchronized (this) {
            if (cache.containsKey(parameter)) {
                return cache.get(parameter);
            }

            try {
                R result = call(parameter);
                cache.put(parameter, result);
                return result;
            } catch (Exception e) {
                if (canCacheForError(e)) {
                    this.e = e;
                }

                throw e;
            }
        }
    }

    @Override
    public R callWithRuntimeException(P parameter) {
        try {
            return call(parameter);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new NestedException(e);
        }
    }

    /**
     * 执行函数并缓存结果，异常包装为RuntimeException
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     */
    public R callWithCacheAndRuntimeException(P parameter) {
        try {
            return callWithCache(parameter);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new NestedException(e);
        }
    }

    /**
     * 重置缓存结果
     */
    public void reset() {
        cache.clear();
        e = null;
    }

    /**
     * 判断是否可缓存异常
     *
     * @param e 异常对象
     * @return true：可缓存，false：不缓存
     */
    private boolean canCacheForError(Exception e) {
        if (cacheForExceptionClasses == null) {
            return false;
        }

        return Arrays.stream(cacheForExceptionClasses)
                .anyMatch(it -> it.isAssignableFrom(e.getClass()));
    }
}