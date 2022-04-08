package org.team4u.base.filter.v2;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.DisableAutoLog;
import org.team4u.base.log.LogMessageContext;

/**
 * 日志拦截器
 *
 * @author jay.wu
 */
public class LogInterceptor implements FilterInterceptor<Object, Filter<Object>> {

    private final Log log = Log.get();

    @Override
    public boolean preHandle(Object context, Filter<Object> filter) {
        if (isDisableLog(filter)) {
            return true;
        }

        LogMessageContext.createAndSet(filter.getClass().getSimpleName(), "doFilter")
                .append("context", context);

        return true;
    }

    @Override
    public void postHandle(Object o, Filter<Object> filter, boolean toNext) {
        if (isDisableLog(filter)) {
            return;
        }

        if (LogMessageContext.get() == null) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug(LogMessageContext.get()
                    .success()
                    .append("toNext", toNext)
                    .toString());
        }
    }

    @Override
    public void afterCompletion(Object o, Filter<Object> filter, Exception e) {
        if (isDisableLog(filter)) {
            return;
        }

        if (LogMessageContext.get() == null) {
            return;
        }

        log.error(e, LogMessageContext.get().fail(e.getMessage()).toString());
    }

    private boolean isDisableLog(Filter<Object> filter) {
        return AnnotationUtil.hasAnnotation(filter.getClass(), DisableAutoLog.class);
    }
}