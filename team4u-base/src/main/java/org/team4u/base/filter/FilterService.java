package org.team4u.base.filter;

/**
 * 过滤器服务
 *
 * @author jay.wu
 */
public interface FilterService<C> {

    /**
     * 开始处理
     *
     * @param context 上下文
     * @return context 上下文
     */
    C doFilter(C context);
}