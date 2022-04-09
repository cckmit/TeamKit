package org.team4u.base.filter.v2;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.log.Log;
import org.team4u.base.log.DisableAutoLog;
import org.team4u.base.log.LogMessage;

/**
 * 日志拦截器
 *
 * @author jay.wu
 */
public class LogInterceptor implements FilterInterceptor<Object, Filter<Object>> {

    private final static String LM_KEY = "LOG_MESSAGE";

    private final Log log = Log.get();

    @Override
    public boolean preHandle(Context<Object, Filter<Object>> context) {
        if (isDisableLog(context.getFilter())) {
            return true;
        }

        LogMessage lm = LogMessage.create(moduleNameOf(context), "doFilter")
                .append("context", context.getFilterContext());
        setLogMessage(context, lm);
        return true;
    }

    private String moduleNameOf(Context<Object, Filter<Object>> context) {
        return context.getFilterChain().getConfig().getName() +
                "|" +
                context.getFilter().getClass().getSimpleName();
    }

    @Override
    public void postHandle(Context<Object, Filter<Object>> context, boolean toNext) {
        if (isDisableLog(context.getFilter())) {
            return;
        }

        if (lm(context) == null) {
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug(lm(context).success().append("toNext", toNext).toString());
        }
    }

    @Override
    public void afterCompletion(Context<Object, Filter<Object>> context, Exception e) {
        if (isDisableLog(context.getFilter())) {
            return;
        }

        if (lm(context) == null) {
            return;
        }

        log.error(e, lm(context).fail(e.getMessage()).toString());
    }

    private boolean isDisableLog(Filter<Object> filter) {
        return AnnotationUtil.hasAnnotation(filter.getClass(), DisableAutoLog.class);
    }

    private void setLogMessage(Context<Object, Filter<Object>> context, LogMessage lm) {
        context.getExt().set(LM_KEY, lm);
    }

    private LogMessage lm(Context<Object, Filter<Object>> context) {
        return (LogMessage) context.getExt().get(LM_KEY);
    }
}