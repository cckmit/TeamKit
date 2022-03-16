package org.team4u.base.filter.v2;

import org.team4u.base.filter.FilterInvoker;
import org.team4u.base.registrar.PolicyRegistrar;

import java.util.List;

/**
 * 拦截器服务
 *
 * @author jay.wu
 */
public class FilterInterceptorService<
        Context,
        F extends Filter<Context>
        > extends PolicyRegistrar<String, FilterInterceptor<?, ?>> {

    public FilterInterceptorService(List<? extends FilterInterceptor<?, ?>> interceptors) {
        registerPolicy(new LogInterceptor());
        registerPolicies(interceptors);
    }

    /**
     * 前置处理
     * <p>
     * 命令执行前
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean preHandle(Context context, F filter) throws Exception {
        for (FilterInterceptor interceptor : policies()) {
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
    public void postHandle(Context context, F filter) throws Exception {
        List<FilterInterceptor<?, ?>> interceptors = policies();
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            FilterInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(context, filter);
        }
    }

    /**
     * 完成处理（无论是否异常最终执行）
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean afterCompletion(Context context, F filter, Exception e) {
        List<FilterInterceptor<?, ?>> interceptors = policies();
        boolean shouldCallNextFilter = true;
        Exception e2 = e;
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            FilterInterceptor interceptor = interceptors.get(i);
            try {
                if (!interceptor.afterCompletion(context, filter, e2)) {
                    shouldCallNextFilter = false;
                }
            } catch (Exception ex) {
                e2 = ex;
            }
        }

        return shouldCallNextFilter;
    }

    /**
     * 执行处理器
     */
    public void apply(Context context,
                      FilterInvoker<Context> invoker,
                      F filter) {
        try {
            if (!preHandle(context, filter)) {
                return;
            }

            boolean isToNextInvoker = filter.doFilter(context);
            postHandle(context, filter);

            if (isToNextInvoker) {
                invoker.invoke(context);
            }
        } catch (Exception e) {
            if (afterCompletion(context, filter, e)) {
                invoker.invoke(context);
            }
        }
    }
}