package org.team4u.command.infrastructure.executor;


import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;
import org.team4u.command.domain.executor.handler.requester.http.HttpCommandRequester;
import org.team4u.command.domain.executor.handler.requester.http.HttpRequester;

public class MockHttpCommandRequester extends HttpCommandRequester {

    private CommandConfig config;
    private CommandRequest request;

    public MockHttpCommandRequester(HttpRequester httpRequester) {
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
    protected HttpRequester.HttpRequest toRequest(Context context) {
        this.config = context.getConfig();
        this.request = context.getRequest();
        return null;
    }

    @Override
    protected CommandResponse toCommandResponse(Context context, HttpRequester.HttpResponse httpResponse) {
        return new MockCommandResponse().setChannelCode(context.getRequest().getCommandId());
    }
}
