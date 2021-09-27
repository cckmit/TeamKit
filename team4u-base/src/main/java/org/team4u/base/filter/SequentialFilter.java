package org.team4u.base.filter;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;

/**
 * 顺序滤器
 * <p>
 * 无论当前过滤器执行结果如何，都将执行下一个过滤器
 * <p>
 * 仅当当前过滤器抛出异常时终止后续流程
 *
 * @author jay.wu
 */
public abstract class SequentialFilter<T> implements Filter<T> {

    private final Log log = Log.get();

    /**
     * 顺序执行下一个过滤器，除非抛出异常
     *
     * @param context           过滤器上下文
     * @param nextFilterInvoker 下一个过滤器执行者
     */
    @Override
    public void doFilter(T context, FilterInvoker<T> nextFilterInvoker) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "doFilter")
                .append("context", context);
        doFilter(context);
        log.info(lm.success().toString());

        nextFilterInvoker.invoke(context);
    }

    /**
     * 进行过滤
     *
     * @param context 过滤器上下文
     */
    protected abstract void doFilter(T context);
}