package org.team4u.base.filter;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessage;

/**
 * 条件过滤器
 * <p>
 * 根据当前过滤器执行结果决定是否执行下一个过滤器
 *
 * @author jay.wu
 */
public abstract class ConditionalFilter<T> implements Filter<T> {

    private final Log log = Log.get();

    @Override
    public void doFilter(T context, FilterInvoker<T> nextFilterInvoker) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "doFilter")
                .append("context", context);

        boolean isInvokeNextFilter = doFilter(context);

        log.debug(lm.success().append("isInvokeNextFilter", isInvokeNextFilter).toString());

        if (isInvokeNextFilter) {
            nextFilterInvoker.invoke(context);
        }
    }

    /**
     * 进行过滤
     *
     * @param context 过滤器上下文
     * @return true则继续处理，false则停止后续处理
     */
    protected abstract boolean doFilter(T context);
}