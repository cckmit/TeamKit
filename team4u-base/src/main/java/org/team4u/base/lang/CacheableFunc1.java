package org.team4u.base.lang;

import cn.hutool.cache.Cache;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.util.ObjectUtil;
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
    private final Cache<Object, R> cache;

    @SuppressWarnings("unchecked")
    public CacheableFunc1(Cache<?, R> cache) {
        this.cache = (Cache<Object, R>) cache;
    }

    /**
     * 执行函数并缓存结果
     *
     * @return 函数执行结果，初次执行后将缓存结果，后续再次执行时直接返回缓存结果
     */
    public R callWithCache(P parameter) {
        Object cacheKey = cacheKey(parameter);
        // 入参相同，且已有结果，直接返回
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        synchronized (this) {
            if (cache.containsKey(cacheKey)) {
                return cache.get(cacheKey);
            }

            LogMessage lm = LogMessage.create(this.getClass().getName(), "callWithCache")
                    .append("parameter", formatParameterForLog(parameter));

            if (!ObjectUtil.equals(cacheKey, parameter)) {
                lm.append("cacheKey", cacheKey);
            }

            try {
                R result = callWithRuntimeException(parameter);
                cache.put(cacheKey, result);

                log.info(lm.success().append("result", formatResultForLog(result)).toString());
                return result;
            } catch (Exception e) {
                log.error(lm.fail(e.getMessage()).toString());
                throw e;
            }
        }
    }

    /**
     * 缓存key，默认为入参值
     *
     * @param parameter 入参值
     * @return 缓存key
     */
    protected Object cacheKey(P parameter) {
        return parameter;
    }

    protected String formatParameterForLog(P parameter) {
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