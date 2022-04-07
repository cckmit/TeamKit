package org.team4u.base.filter.v2;

/**
 * 过滤器
 *
 * @author jay.wu
 */
public interface Filter<T> {
    /**
     * 跳过处理器
     * <p>
     * 不执行任何逻辑，跳过执行下一个处理器
     */
    @SuppressWarnings("rawtypes")
    Filter SKIP_FILTER = context -> true;

    /**
     * 终止处理器
     * <p>
     * 不执行任何逻辑，终止后续执行
     */
    @SuppressWarnings("rawtypes")
    Filter END_FILTER = context -> false;

    /**
     * 进行过滤
     *
     * @param context 过滤器上下文
     * @return 是否继续执行下一个过滤器
     */
    boolean doFilter(T context);
}