package org.team4u.base.filter.v2;


import org.team4u.base.registrar.StringIdPolicy;

/**
 * 处理拦截器
 *
 * @author jay.wu
 */
public interface FilterInterceptor<Context, F extends Filter<Context>> extends StringIdPolicy {

    /**
     * 前置处理
     * <p>
     * 命令执行前
     *
     * @return 是否继续执行下一个过滤器
     */
    boolean preHandle(Context context, F filter);

    /**
     * 后置处理（非异常情况执行）
     * <p>
     * 命令执行后
     */
    void postHandle(Context context, F filter, boolean toNext);

    /**
     * 完成处理（异常情况执行）
     */
    void afterCompletion(Context context, F filter, Exception e);

    @Override
    default String id() {
        return this.getClass().getSimpleName();
    }
}