package org.team4u.base.filter;

import cn.hutool.core.collection.CollUtil;

import java.util.Collections;
import java.util.List;

/**
 * 过滤器责任链
 *
 * @param <C> 上下文类型
 * @author jay.wu
 */
public class FilterChain<C> {

    private final Invoker invoker;
    private final List<? extends Filter<C>> filters;

    public FilterChain(List<? extends Filter<C>> filters) {
        this.invoker = new Invoker();
        this.filters = Collections.unmodifiableList(filters);
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

    /**
     * 获取指定位置的过滤器
     *
     * @param index 索引位置
     * @return 过滤器。若无法找到，则返回null
     */
    public Filter<C> filter(int index) {
        return CollUtil.get(filters(), index);
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
     * 执行过滤
     *
     * @param context 上下文
     */
    public void doFilter(C context) {
        invoker.invoke(context);
    }

    private class Invoker implements FilterInvoker<C> {
        /**
         * 当前过滤器索引
         */
        private int index = 0;

        @Override
        public void invoke(C context) {
            Filter<C> filter = filter(index);
            if (filter == null) {
                return;
            }

            index++;
            filter.doFilter(context, this);
        }
    }
}