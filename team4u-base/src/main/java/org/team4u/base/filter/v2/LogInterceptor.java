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
        LogMessageContext.createAndSet(filter.getClass().getSimpleName(), "doFilter").append("context", context);

        return true;
    }

    @Override
    public void postHandle(Object o, Filter filter) {
        if (log.isDebugEnabled()) {
            log.debug(LogMessageContext.get().success().toString());
        }
    }

    @Override
    public boolean afterCompletion(Object o, Filter filter, Exception e) throws Exception {
        log.error(e, LogMessageContext.get().fail(e.getMessage()).toString());
        throw e;
    }
}