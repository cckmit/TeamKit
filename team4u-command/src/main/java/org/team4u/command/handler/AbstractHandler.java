package org.team4u.command.handler;

import com.alibaba.fastjson.JSON;
import org.team4u.command.Handler;
import org.team4u.command.HandlerInterceptorService;
import org.team4u.base.error.NestedException;
import org.team4u.base.filter.FilterInvoker;
import org.team4u.base.lang.EasyMap;
import org.team4u.template.TemplateEngine;

/**
 * 抽象处理器
 *
 * @author jay.wu
 */
public abstract class AbstractHandler implements Handler {

    private final TemplateEngine templateEngine;
    private final HandlerInterceptorService handlerInterceptorService;

    public AbstractHandler(TemplateEngine templateEngine,
                           HandlerInterceptorService handlerInterceptorService) {
        this.templateEngine = templateEngine;
        this.handlerInterceptorService = handlerInterceptorService;
    }

    @Override
    public void doFilter(Context context, FilterInvoker<Context> nextFilter) {
        // 判断是否最后一个处理器
        if (nextFilter == FilterInvoker.EMPTY_INVOKER) {
            context.setLastHandler(true);
        }

        boolean canGo = handlerInterceptorService.apply(context, this, (c) -> {
            handle(renderConfig(c), c.attributes());
        });

        if (canGo) {
            nextFilter.invoke(context);
        }
    }

    /**
     * 处理逻辑
     *
     * @param config     处理器配置
     * @param attributes 上下文属性
     */
    protected abstract void handle(EasyMap config, EasyMap attributes);

    private EasyMap renderConfig(Context context) {
        EasyMap currentConfig = context.config().configOf(id());
        if (currentConfig == null) {
            return new EasyMap();
        }

        String configStr = templateEngine.render(JSON.toJSONString(currentConfig), context.attributes());

        return JSON.parseObject(configStr, EasyMap.class);
    }

    protected RuntimeException toRuntimeException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }

        return new NestedException(e);
    }
}