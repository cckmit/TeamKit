package org.team4u.command.domain.executor.handler.requester;

import org.team4u.command.infrastructure.util.ExtractConfig;
import org.team4u.command.infrastructure.util.XmlExtractor;

/**
 * Xml提取器处理器
 *
 * @author jay.wu
 */
public class XmlExtractorHandler extends JsonExtractorHandler {

    public Object extract(Context context, String body) {
        ExtractConfig extractConfig = context.getConfig().itemOf(
                "xmlExtractor",
                ExtractConfig.class
        );

        return XmlExtractor.getInstance().extract(extractConfig, body);
    }
}