package org.team4u.base.filter;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 * 过滤器责任链
 *
 * @param <C> 上下文类型
 * @author jay.wu
 */
public class FilterChain<C> {

    private final List<? extends Filter<C>> filters;
    /**
     * 第一个过滤器执行者
     */
    private FilterInvoker<C> header;

    @SuppressWarnings("unchecked")
    public FilterChain(List<? extends Filter<C>> filters) {
        this.filters = filters;

        FilterInvoker<C> last = FilterInvoker.EMPTY_INVOKER;

        if (CollUtil.isNotEmpty(filters)) {
            for (int i = filters.size() - 1; i >= 0; i--) {
                FilterInvoker<C> next = last;
                Filter<C> filter = filters.get(i);
                last = context -> filter.doFilter(context, next);
            }
        }

        setHeader(last);
    }

    /**
     * 执行过滤
     *
     * @param context 过滤上下文
     */
    public void doFilter(C context) {
        if (header == null) {
            return;
        }

        header.invoke(context);
    }

    protected void setHeader(FilterInvoker<C> header) {
        this.header = header;
    }

    /**
     * 获取所有过滤器
     *
     * @return 过滤器集合
     */
    public List<? extends Filter<C>> filters() {
        return filters;
    }

    /**
     * 构建责任链
     *
     * @param filters 过滤器集合
     * @param <C>     上下文类型
     * @return 责任链
     */
    public static <C> FilterChain<C> create(Filter<C>... filters) {
        return create(CollUtil.toList(filters));
    }

    /**
     * 创建责任链
     *
     * @param filters 过滤器集合
     * @param <C>     上下文类型
     * @return 责任链
     */
    public static <C> FilterChain<C> create(List<? extends Filter<C>> filters) {
        return new FilterChain<>(filters);
    }
}