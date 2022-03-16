package org.team4u.base.filter.v2;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.filter.FilterInvoker;

import java.util.Collections;
import java.util.List;

/**
 * 过滤器责任链
 *
 * @param <C> 上下文类型
 * @author jay.wu
 */
public class FilterChain<C, F extends Filter<C>> {

    /**
     * 第一个过滤器执行者
     */
    private FilterInvoker<C> header;

    private final List<F> filters;

    private final FilterInterceptorService<C, F> interceptorService;

    public FilterChain(List<F> filters,
                       List<? extends FilterInterceptor<?, ?>> interceptors) {
        this.filters = filters;
        this.interceptorService = new FilterInterceptorService<>(interceptors);

        initFilterChain();
    }

    @SuppressWarnings("unchecked")
    private void initFilterChain() {
        FilterInvoker<C> last = FilterInvoker.EMPTY_INVOKER;

        if (CollUtil.isNotEmpty(filters)) {
            for (int i = filters.size() - 1; i >= 0; i--) {
                FilterInvoker<C> next = last;
                F filter = filters.get(i);
                last = context -> interceptorService.apply(context, next, filter);
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
    public List<F> filters() {
        return Collections.unmodifiableList(filters);
    }

    /**
     * 构建责任链
     *
     * @param filters 过滤器集合
     * @param <C>     上下文类型
     * @return 责任链
     */
    @SafeVarargs
    public static <C, F extends Filter<C>> FilterChain<C, F> create(F... filters) {
        return create(CollUtil.toList(filters));
    }

    /**
     * 创建责任链
     *
     * @param filters 过滤器集合
     * @param <C>     上下文类型
     * @return 责任链
     */
    public static <C, F extends Filter<C>> FilterChain<C, F> create(List<F> filters) {
        return new FilterChain<>(filters, Collections.emptyList());
    }

    /**
     * 创建责任链
     *
     * @param filters      过滤器集合
     * @param interceptors 拦截器集合
     * @param <C>          上下文类型
     * @return 责任链
     */
    public static <C, F extends Filter<C>> FilterChain<C, F> create(List<F> filters,
                                                                    List<? extends FilterInterceptor<?, ?>> interceptors) {
        return new FilterChain<>(filters, interceptors);
    }
}