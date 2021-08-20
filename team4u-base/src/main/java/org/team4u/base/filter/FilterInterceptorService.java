package org.team4u.base.filter;

import cn.hutool.core.lang.func.VoidFunc1;
import org.team4u.base.error.NestedException;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.registrar.PolicyRegistrar;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拦截器服务
 *
 * @author jay.wu
 */
public class FilterInterceptorService<Context,
        F extends Filter<Context>,
        Interceptor extends FilterInterceptor<Context, F>>
        extends PolicyRegistrar<String, Interceptor> {

    public FilterInterceptorService() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    public FilterInterceptorService(List<Interceptor> objects) {
        super(objects);
    }

    /**
     * 前置处理
     * <p>
     * 命令执行前
     */
    public boolean preHandle(Context context, List<String> interceptorIds, F filter) throws Exception {
        for (Interceptor interceptor : interceptorsOf(interceptorIds)) {
            if (!interceptor.preHandle(context, filter)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 后置处理（非异常情况执行）
     * <p>
     * 命令执行后
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void postHandle(Context context, List<String> interceptorIds, F filter) throws Exception {
        List<Interceptor> interceptors = interceptorsOf(interceptorIds);
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            FilterInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(context, filter);
        }
    }

    /**
     * 完成处理（无论是否异常最终执行）
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean afterCompletion(Context context, List<String> interceptorIds, F filter, Exception ex) {
        List<Interceptor> interceptors = interceptorsOf(interceptorIds);
        boolean shouldCallNextFilter = true;

        for (int i = interceptors.size() - 1; i >= 0; i--) {
            FilterInterceptor interceptor = interceptors.get(i);
            try {
                if (!interceptor.afterCompletion(context, filter, ex)) {
                    shouldCallNextFilter = false;
                }
            } catch (Exception e) {
                throw toRuntimeException(e);
            }
        }

        return shouldCallNextFilter;
    }

    /**
     * 根据拦截器标识集合查找拦截器
     *
     * @param interceptorIds 拦截器标识集合
     * @return 拦截器集合
     */
    public List<Interceptor> interceptorsOf(List<String> interceptorIds) {
        if (interceptorIds == null) {
            return Collections.emptyList();
        }

        return interceptorIds.stream()
                .map(it -> {
                    Interceptor interceptor = policyOf(it);
                    if (interceptor == null) {
                        throw new SystemDataNotExistException("FilterInterceptorId=" + it);
                    }
                    return interceptor;
                })
                .collect(Collectors.toList());
    }

    /**
     * 执行处理器
     *
     * @param context 上下文
     * @param filter  处理器
     * @param worker  处理内容
     * @return 是否允许继续执行下一个处理器, true:允许，false:不允许
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean apply(Context context,
                         List<String> interceptorIds,
                         F filter,
                         VoidFunc1 worker) {
        try {
            if (!preHandle(context, interceptorIds, filter)) {
                return afterCompletion(context, interceptorIds, filter, null);
            }

            worker.call(context);

            postHandle(context, interceptorIds, filter);

            return afterCompletion(context, interceptorIds, filter, null);
        } catch (Exception e) {
            return afterCompletion(context, interceptorIds, filter, e);
        }
    }

    private RuntimeException toRuntimeException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }

        return new NestedException(e);
    }
}