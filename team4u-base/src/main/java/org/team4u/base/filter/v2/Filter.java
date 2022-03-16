package org.team4u.base.filter.v2;

/**
 * 过滤器
 *
 * @author jay.wu
 */
public interface Filter<T> {

    /**
     * 进行过滤
     *
     * @param context 过滤器上下文
     * @return 是否继续执行下一个过滤器
     */
    boolean doFilter(T context);
}