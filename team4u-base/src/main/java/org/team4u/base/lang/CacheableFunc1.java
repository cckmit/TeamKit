package org.team4u.base.lang;

import cn.hutool.cache.Cache;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.log.Log;
import org.team4u.base.error.NestedException;
import org.team4u.base.log.LogMessage;

/**
 * 可缓存结果的单参数函数
 *
 * @param <P> 入参类型
 * @param <R> 返回值类型
 * @author jay.wu
 */
public abstract class CacheableFunc1<P, R> implements Func1<P, R> {

    private final Log log = Log.get();

    /**
     * 缓存的执行结果
     */
    private final Cache<P, R> cache;

    public CacheableFunc1(Cache<P, R> cache) {
        this.cache = cache;
    }

    /**
     * 执行函数并缓存结果
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     */
    public R callWithCache(P parameter) {
        // 入参相同，且已有结果，直接返回
        if (cache.containsKey(parameter)) {
            return cache.get(parameter);
        }

        synchronized (this) {
            if (cache.containsKey(parameter)) {
                return cache.get(parameter);
            }

            R result = callWithRuntimeException(parameter);
            cache.put(parameter, result);

            logForCall(parameter);
            return result;
        }
    }

    private void logForCall(P parameter) {
        if (log.isDebugEnabled()) {
            log.debug(LogMessage.create(this.getClass().getName(), "callWithCache")
                    .append("parameter", Convert.toStr(parameter))
                    .success()
                    .toString());
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
     * 删除缓存
     *
     * @param parameter 入参
     */
    public void removeCache(P parameter) {
        cache.remove(parameter);
    }

    /**
     * 重置缓存结果
     */
    public void reset() {
        cache.clear();
    }
}