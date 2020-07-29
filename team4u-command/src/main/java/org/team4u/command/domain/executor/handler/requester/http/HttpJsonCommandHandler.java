package org.team4u.command.domain.executor.handler.requester.http;

import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandResponse;
import org.team4u.command.infrastructure.util.JsonExtractor;

public abstract class HttpJsonCommandHandler extends HttpCommandHandler {

    protected HttpJsonCommandHandler(HttpRequester httpRequester) {
        super(httpRequester);
    }

    @Override
    protected CommandResponse toCommandResponse(CommandConfig config, HttpResponse response) {
        String body = response.getBody();
        JsonExtractor.ExtractConfig extractConfig = config.itemOf(
                "jsonExtractorConfig",
                JsonExtractor.ExtractConfig.class
        );
        return new JsonExtractor().extract(extractConfig, body);
    }
}