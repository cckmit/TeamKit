package org.team4u.base.filter;

/**
 * 过滤器
 *
 * @author jay.wu
 */
public interface Filter<T> {

    /**
     * 进行过滤
     *
     * @param context    过滤上下文
     * @param nextFilter 下一个过滤器执行者
     */
    void doFilter(T context, FilterInvoker<T> nextFilter);

}