package org.team4u.base.filter;

/**
 * 过滤器执行者
 *
 * @author jay.wu
 */
public interface FilterInvoker<T> {
    /**
     * 空执行器
     */
    @SuppressWarnings("rawtypes")
    FilterInvoker EMPTY_INVOKER = context -> {
    };

    /**
     * 执行过滤器
     *
     * @param context 过滤上下文
     */
    void invoke(T context);
}