package org.team4u.base.filter;

/**
 * 过滤器
 * <p>
 * Deprecated，use org.team4u.base.filter.v2.Filter instead
 *
 * @author jay.wu
 * @see org.team4u.base.filter.v2.Filter
 */
@Deprecated
public interface Filter<T> {

    /**
     * 进行过滤
     *
     * @param context           过滤器上下文
     * @param nextFilterInvoker 下一个过滤器执行者
     */
    void doFilter(T context, FilterInvoker<T> nextFilterInvoker);
}