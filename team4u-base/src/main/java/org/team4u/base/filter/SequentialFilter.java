package org.team4u.base.filter;

/**
 * 顺序滤器
 *
 * 无论当前过滤器执行结果如何，都将执行下一个过滤器
 *
 * 仅当当前过滤器抛出异常时终止后续流程
 *
 * @author jay.wu
 */
public abstract class SequentialFilter<T> implements Filter<T> {

    /**
     * 顺序执行下一个过滤器，除非抛出异常
     *
     * @param context           过滤器上下文
     * @param nextFilterInvoker 下一个过滤器执行者
     */
    @Override
    public void doFilter(T context, FilterInvoker<T> nextFilterInvoker) {
        doFilter(context);
        nextFilterInvoker.invoke(context);
    }

    /**
     * 进行过滤
     *
     * @param context 过滤器上下文
     */
    protected abstract void doFilter(T context);
}