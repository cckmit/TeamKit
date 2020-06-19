package org.team4u.command;

import cn.hutool.core.lang.func.VoidFunc1;
import org.team4u.core.filter.FilterInterceptorService;

import java.util.List;

/**
 * 拦截器服务
 *
 * @author jay.wu
 */
public class HandlerInterceptorService extends FilterInterceptorService<Handler.Context, Handler, HandlerInterceptor> {

    public HandlerInterceptorService(List<HandlerInterceptor> objects) {
        super(objects);
    }

    /**
     * 执行处理器
     *
     * @param context 上下文
     * @param handler 处理器
     * @param worker  处理内容
     * @return 是否允许继续执行下一个处理器, true:允许，false:不允许
     */
    public boolean apply(Handler.Context context,
                         Handler handler,
                         VoidFunc1<Handler.Context> worker) {
        return apply(context, context.config().getInterceptors(), handler, worker);
    }
}