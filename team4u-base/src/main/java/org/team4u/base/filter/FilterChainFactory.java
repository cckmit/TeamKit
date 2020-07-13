package org.team4u.base.filter;

import cn.hutool.core.collection.CollUtil;

import java.util.List;

/**
 * 责任链模式工厂
 *
 * @author jay.wu
 */
public class FilterChainFactory {

    /**
     * 构建责任链
     *
     * @param filters 过滤器集合
     * @param <T>     过滤上下文类型
     * @return 责任链
     */
    public static <T> FilterChain<T> buildFilterChain(Filter<T>... filters) {
        return buildFilterChain(CollUtil.toList(filters));
    }

    /**
     * 构建责任链
     *
     * @param filters 过滤器集合
     * @param <T>     过滤上下文类型
     * @return 责任链
     */
    public static <T> FilterChain<T> buildFilterChain(List<? extends Filter<T>> filters) {
        FilterChain<T> filterChain = new FilterChain<>();

        if (CollUtil.isEmpty(filters)) {
            return filterChain;
        }

        @SuppressWarnings("unchecked") FilterInvoker<T> last = FilterInvoker.EMPTY_INVOKER;
        for (int i = filters.size() - 1; i >= 0; i--) {
            FilterInvoker<T> next = last;
            Filter<T> filter = filters.get(i);
            last = context -> filter.doFilter(context, next);
        }

        filterChain.setHeader(last);
        return filterChain;
    }
}