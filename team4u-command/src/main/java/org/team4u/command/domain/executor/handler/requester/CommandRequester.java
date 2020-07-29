package org.team4u.command.domain.executor.handler.requester;


import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandHandler;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;

public abstract class CommandRequester<Request, Response> implements CommandHandler {

    @Override
    public void handle(Context context) {
        Response response = execute(toRequest(context.getConfig(), context.getRequest()));
        context.setResponse(toCommandResponse(context.getConfig(), response));
    }

    protected abstract Response execute(Request request);

    protected abstract Request toRequest(CommandConfig config, CommandRequest commandRequest);

    protected abstract CommandResponse toCommandResponse(CommandConfig config, Response response);
}