package org.team4u.command.infrastructure.executor;


import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;
import org.team4u.command.domain.executor.handler.requester.http.HttpCommandHandler;
import org.team4u.command.domain.executor.handler.requester.http.HttpRequester;

public class MockHttpCommandHandler extends HttpCommandHandler {

    private CommandConfig config;
    private CommandRequest request;

    public MockHttpCommandHandler(HttpRequester httpRequester) {
        super(httpRequester);
    }

    public CommandConfig getConfig() {
        return config;
    }

    public CommandRequest getRequest() {
        return request;
    }

    @Override
    public HttpRequester.HttpResponse execute(HttpRequester.HttpRequest request) {
        return null;
    }

    @Override
    protected HttpRequester.HttpRequest toRequest(CommandConfig config, CommandRequest commandRequest) {
        this.config = config;
        this.request = commandRequest;
        return null;
    }

    @Override
    protected CommandResponse toCommandResponse(CommandConfig config, HttpRequester.HttpResponse httpResponse) {
        return new MockCommandResponse().setChannelCode(request.getCommandId());
    }
}
