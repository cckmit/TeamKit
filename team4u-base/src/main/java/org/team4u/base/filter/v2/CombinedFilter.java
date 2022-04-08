package org.team4u.base.filter.v2;

/**
 * 组合过滤器
 * <p>
 * 内部执行子责任链流程
 *
 * @author jay.wu
 */
public class CombinedFilter<C> implements Filter<C> {

    private final FilterChain<C, Filter<C>> filterChain;

    public CombinedFilter(FilterChain.Config config) {
        filterChain = FilterChain.create(config);
    }

    @Override
    public boolean doFilter(C context) {
        filterChain.doFilter(context);
        return true;
    }

    public static <C> CombinedFilter<C> create(FilterChain.Config config) {
        return new CombinedFilter<>(config);
    }
}