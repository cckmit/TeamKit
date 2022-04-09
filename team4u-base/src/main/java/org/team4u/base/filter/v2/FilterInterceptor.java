package org.team4u.base.filter.v2;


import cn.hutool.core.lang.Dict;
import lombok.Data;
import org.team4u.base.registrar.StringIdPolicy;

/**
 * 处理拦截器
 *
 * @author jay.wu
 */
public interface FilterInterceptor<C, F extends Filter<C>> extends StringIdPolicy {

    /**
     * 前置处理
     * <p>
     * 命令执行前
     *
     * @return 是否继续执行下一个过滤器
     */
    boolean preHandle(Context<C, F> context);

    /**
     * 后置处理（非异常情况执行）
     * <p>
     * 命令执行后
     */
    void postHandle(Context<C, F> context, boolean toNext);

    /**
     * 完成处理（异常情况执行）
     */
    void afterCompletion(Context<C, F> context, Exception e);

    @Override
    default String id() {
        return this.getClass().getSimpleName();
    }

    @Data
    class Context<C, F extends Filter<C>> {

        private final FilterChain<C, F> filterChain;

        private final C filterContext;

        private final F filter;

        private Dict ext = Dict.create();
    }
}