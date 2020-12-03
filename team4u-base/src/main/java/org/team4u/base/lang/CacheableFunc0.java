package org.team4u.base.lang;

import cn.hutool.core.lang.func.Func0;
import org.team4u.base.error.NestedException;

import java.util.Arrays;

/**
 * 可缓存结果的无参数函数对象
 *
 * @param <R> 返回值类型
 * @author jay.wu
 */
public abstract class CacheableFunc0<R> implements Func0<R> {

    /**
     * 未初始化结果值
     */
    private static final Object NOT_INIT = new Object();

    private final Class<?>[] cacheForExceptionClasses;

    /**
     * 缓存的执行结果
     */
    private Object result = NOT_INIT;
    /**
     * 缓存的执行异常
     */
    private Exception e;

    public CacheableFunc0() {
        this(null);
    }

    /**
     * @param cacheForExceptionClasses 需要缓存的异常类型，为null时不缓存
     */
    public CacheableFunc0(Class<?>[] cacheForExceptionClasses) {
        this.cacheForExceptionClasses = cacheForExceptionClasses;
    }

    /**
     * 执行函数并缓存结果
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     * @throws Exception 自定义异常
     */
    @SuppressWarnings("unchecked")
    public R callWithCache() throws Exception {
        if (e != null) {
            throw e;
        }

        if (result != NOT_INIT) {
            return (R) result;
        }

        try {
            result = call();
            return (R) result;
        } catch (Exception e) {
            if (canCacheForError(e)) {
                this.e = e;
            }

            throw e;
        }
    }

    @Override
    public R callWithRuntimeException() {
        try {
            return call();
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
    public R callWithCacheAndRuntimeException() {
        try {
            return callWithCache();
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
        result = NOT_INIT;
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