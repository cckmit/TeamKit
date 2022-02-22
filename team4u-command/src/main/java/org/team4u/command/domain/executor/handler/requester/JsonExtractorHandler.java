package org.team4u.command.domain.executor.handler.requester;

import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.command.infrastructure.util.ExtractConfig;
import org.team4u.command.infrastructure.util.JsonExtractor;

/**
 * Json提取器处理器
 *
 * @author jay.wu
 */
public class JsonExtractorHandler implements CommandHandler {

    @Override
    public void handle(Context context) {
        String body = context.getResponse();
        context.setResponse(extract(context, body));
    }

    public Object extract(Context context, String body) {
        ExtractConfig extractConfig = context.getConfig().itemOf(
                "jsonExtractor",
                ExtractConfig.class
        );

        return JsonExtractor.getInstance().extract(extractConfig, body);
    }
}