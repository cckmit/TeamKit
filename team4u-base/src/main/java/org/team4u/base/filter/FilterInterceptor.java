package org.team4u.base.filter;


import org.team4u.base.lang.IdObject;

/**
 * 处理拦截器
 *
 * @author jay.wu
 */
public interface FilterInterceptor<Context, F extends Filter<Context>> extends IdObject<String> {

    /**
     * 前置处理
     * <p>
     * 命令执行前
     */
    boolean preHandle(Context context, F filter) throws Exception;

    /**
     * 后置处理（非异常情况执行）
     * <p>
     * 命令执行后
     */
    void postHandle(Context context, F filter) throws Exception;

    /**
     * 完成处理（无论是否异常最终执行）
     */
    void afterCompletion(Context context, F filter, Exception ex) throws Exception;
}