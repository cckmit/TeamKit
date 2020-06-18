package org.team4u.core.log;

import java.util.Map;

/**
 * 简单日志消息渲染器
 *
 * @author jay.wu
 */
public class SimpleLogMessageRender implements LogMessageRender {

    @Override
    public String render(LogMessage logMessage) {
        LogStringBuilder builder = new LogStringBuilder(logMessage.config());

        renderHeader(logMessage, builder);
        renderSpendTime(logMessage, builder);
        renderBody(logMessage, builder);

        return builder.toString();
    }

    protected void renderHeader(LogMessage logMessage, LogStringBuilder builder) {
        builder.append(logMessage.getModule())
                .appendWithSeparator(logMessage.getEventName());

        if (logMessage.getResult() != null) {
            builder.appendWithSeparator(logMessage.getResult());
        }
    }

    protected void renderSpendTime(LogMessage logMessage, LogStringBuilder builder) {
        long spendTime = logMessage.spendTime();
        if (spendTime >= logMessage.config().getMinSpendTimeMillsToDisplay()) {
            builder.appendWithSeparator(spendTime).append("ms");
        }
    }

    protected void renderBody(LogMessage logMessage, LogStringBuilder builder) {
        for (Map.Entry<String, Object> entry : logMessage.fieldValues().entrySet()) {
            builder.appendWithSeparator(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }
    }

    public static class LogStringBuilder {

        StringBuilder stringBuilder;
        LogMessageConfig config;

        public LogStringBuilder(LogMessageConfig config) {
            this.stringBuilder = new StringBuilder();
            this.config = config;
        }

        public LogStringBuilder append(Object value) {
            stringBuilder.append(value);
            return this;
        }

        public LogStringBuilder separator() {
            stringBuilder.append(config.getSeparator());
            return this;
        }

        public LogStringBuilder appendWithSeparator(Object value) {
            return separator().append(value);
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}