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
 * <p>
 * 注意，结果为null不会进行缓存，请用其他对象代替null的含义
 *
 * @param <P> 入参类型
 * @param <R> 返回值类型
 * @author jay.wu
 * @see org.team4u.base.lang.lazy.LazyFunction
 * @deprecated 使用LazyFunction代替
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
        R result = cache.get(cacheKey);
        if (result != null) {
            return result;
        }

        synchronized (this) {
            result = cache.get(cacheKey);
            if (result != null) {
                return result;
            }

            LogMessage lm = LogMessage.create(this.getClass().getName(), "callWithCache")
                    .append("parameter", formatParameterForLog(parameter));

            if (!ObjectUtil.equals(cacheKey, parameter)) {
                lm.append("cacheKey", cacheKey);
            }

            try {
                result = callWithRuntimeException(parameter);

                if (result == null) {
                    throw new IllegalStateException("Cache value can not be null!");
                }

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