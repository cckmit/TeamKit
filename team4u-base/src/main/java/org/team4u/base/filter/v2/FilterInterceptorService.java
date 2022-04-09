package org.team4u.base.filter.v2;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.NestedException;
import org.team4u.base.filter.FilterInvoker;
import org.team4u.base.registrar.PolicyRegistrar;

import java.util.List;

/**
 * 拦截器服务
 *
 * @author jay.wu
 */
public class FilterInterceptorService<C, F extends Filter<C>>
        extends PolicyRegistrar<String, FilterInterceptor<?, ?>> {

    public FilterInterceptorService(List<? extends FilterInterceptor<?, ?>> interceptors) {
        registerPolicies(interceptors);
        registerDefaultInterceptors(interceptors);
    }

    private void registerDefaultInterceptors(List<? extends FilterInterceptor<?, ?>> interceptors) {
        registerDefaultInterceptor(interceptors, new LogInterceptor());
    }

    private void registerDefaultInterceptor(List<? extends FilterInterceptor<?, ?>> interceptors,
                                            FilterInterceptor<?, ?> defaultInterceptor) {
        // 若已有相同标识的自定义拦截器，则不再注册默认拦截器
        if (interceptors.stream().noneMatch(it -> StrUtil.equals(it.id(), defaultInterceptor.id()))) {
            registerPolicy(defaultInterceptor);
        }
    }

    /**
     * 前置处理
     * <p>
     * 命令执行前
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean preHandle(FilterInterceptor.Context context) {
        return policies().stream().allMatch(it ->
                it.preHandle(context)
        );
    }

    /**
     * 后置处理（非异常情况执行）
     * <p>
     * 命令执行后
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void postHandle(FilterInterceptor.Context context, boolean toNext) {
        CollUtil.reverse(policies()).forEach(it ->
                it.postHandle(context, toNext)
        );
    }

    /**
     * 完成处理（异常情况执行）
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void afterCompletion(FilterInterceptor.Context context, Exception e) {
        CollUtil.reverse(policies()).forEach(it ->
                it.afterCompletion(context, e)
        );
    }

    /**
     * 执行处理器
     */
    public void apply(FilterChain<C, F> filterChain, C filterContext, FilterInvoker<C> invoker, F filter) {
        FilterInterceptor.Context<C, F> context = new FilterInterceptor.Context<>(
                filterChain,
                filterContext,
                filter
        );

        try {
            if (!preHandle(context)) {
                return;
            }

            boolean toNext = filter.doFilter(filterContext);
            postHandle(context, toNext);

            if (toNext) {
                invoker.invoke(filterContext);
            }
        } catch (Exception e) {
            afterCompletion(context, e);
            throw NestedException.wrap(e);
        }
    }
}