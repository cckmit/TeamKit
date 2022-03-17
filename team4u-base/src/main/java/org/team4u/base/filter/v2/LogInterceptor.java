package org.team4u.base.filter.v2;

import cn.hutool.log.Log;
import org.team4u.base.log.LogMessageContext;

/**
 * 日志拦截器
 *
 * @author jay.wu
 */
@SuppressWarnings("rawtypes")
public class LogInterceptor implements FilterInterceptor {

    private final Log log = Log.get();

    @Override
    public boolean preHandle(Object context, Filter filter) {
        LogMessageContext.createAndSet(filter.getClass().getSimpleName(), "doFilter")
                .append("context", context);

        return true;
    }

    @Override
    public void postHandle(Object o, Filter filter, boolean toNext) {
        if (log.isDebugEnabled()) {
            log.debug(LogMessageContext.get()
                    .success()
                    .append("toNext", toNext)
                    .toString());
        }
    }

    @Override
    public void afterCompletion(Object o, Filter filter, Exception e) {
        log.error(e, LogMessageContext.get().fail(e.getMessage()).toString());
    }
}