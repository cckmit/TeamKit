package org.team4u.command.infrastructure.executor.camel;


import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;
import org.team4u.command.domain.executor.handler.requester.http.HttpCommandHandler;
import org.team4u.command.domain.executor.handler.requester.http.HttpRequest;
import org.team4u.command.domain.executor.handler.requester.http.HttpRequester;
import org.team4u.command.domain.executor.handler.requester.http.HttpResponse;

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
    public HttpResponse execute(HttpRequest request) {
        return null;
    }

    @Override
    protected HttpRequest toRequest(CommandConfig config, CommandRequest commandRequest) {
        this.config = config;
        this.request = commandRequest;
        return null;
    }

    @Override
    protected CommandResponse toCommandResponse(CommandConfig config, HttpResponse httpResponse) {
        return new MockCommandResponse().setChannelCode(request.getCommandId());
    }
}
