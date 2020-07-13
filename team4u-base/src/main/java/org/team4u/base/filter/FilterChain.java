package org.team4u.base.filter;

/**
 * 过滤器责任链
 *
 * @author jay.wu
 */
public class FilterChain<T> {

    /**
     * 第一个过滤器执行者
     */
    private FilterInvoker<T> header;

    /**
     * 执行过滤
     *
     * @param context 过滤上下文
     */
    public void doFilter(T context) {
        if (header == null) {
            return;
        }

        header.invoke(context);
    }

    public void setHeader(FilterInvoker<T> header) {
        this.header = header;
    }
}