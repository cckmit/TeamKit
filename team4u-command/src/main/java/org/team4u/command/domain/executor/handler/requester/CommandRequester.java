package org.team4u.command.domain.executor.handler.requester;


import org.team4u.command.domain.executor.handler.SimpleCommandHandler;

/**
 * 命令请求者
 *
 * @author jay.wu
 */
public abstract class CommandRequester<Request, Response> implements SimpleCommandHandler {

    @Override
    public void internalHandle(Context context) {
        Response response = execute(toRequest(context));
        context.setResponse(toCommandResponse(context, response));
    }

    /**
     * 执行请求
     *
     * @param request 请求者特定的请求对象
     * @return 请求者特定的响应对象
     */
    protected abstract Response execute(Request request);

    /**
     * 将命令请求转换为请求者特定的请求对象
     *
     * @param context 命令上下文
     * @return 请求者特定的请求对象
     */
    protected abstract Request toRequest(Context context);

    /**
     * 将请求者特定的响应对象转换命令对象
     *
     * @param context  命令上下文
     * @param response 请求者特定的响应对象
     * @return 请求者特定的请求对象
     */
    protected abstract Object toCommandResponse(Context context, Response response);
}