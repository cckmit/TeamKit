package org.team4u.base.filter.v2;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.error.NestedException;
import org.team4u.base.filter.FilterInvoker;
import org.team4u.base.registrar.PolicyRegistrar;

import java.util.List;

/**
 * 拦截器服务
 *
 * @author jay.wu
 */
public class FilterInterceptorService<Context, F extends Filter<Context>>
        extends PolicyRegistrar<String, FilterInterceptor<?, ?>> {

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
    public boolean preHandle(Context context, F filter) {
        return policies().stream().allMatch(it ->
                ((FilterInterceptor) it).preHandle(context, filter)
        );
    }

    /**
     * 后置处理（非异常情况执行）
     * <p>
     * 命令执行后
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void postHandle(Context context, F filter, boolean toNext) {
        CollUtil.reverse(policies()).forEach(it ->
                ((FilterInterceptor) it).postHandle(context, filter, toNext)
        );
    }

    /**
     * 完成处理（无论是否异常最终执行）
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void afterCompletion(Context context, F filter, Exception e) {
        CollUtil.reverse(policies()).forEach(it ->
                ((FilterInterceptor) it).afterCompletion(context, filter, e)
        );

        throw NestedException.wrap(e);
    }

    /**
     * 执行处理器
     */
    public void apply(Context context, FilterInvoker<Context> invoker, F filter) {
        try {
            if (!preHandle(context, filter)) {
                return;
            }

            boolean toNext = filter.doFilter(context);
            postHandle(context, filter, toNext);

            if (toNext) {
                invoker.invoke(context);
            }
        } catch (Exception e) {
            afterCompletion(context, filter, e);
        }
    }
}