package org.team4u.kit.core.log;

import java.util.Map;

public class SimpleLogMessageRender implements LogMessageRender {

    @Override
    public String render(LogMessage logMessage) {
        LogStringBuilder builder = new LogStringBuilder(logMessage.config());

        renderHeader(logMessage, builder);
        renderSpendTime(logMessage, builder);
        renderBody(logMessage, builder);

        return builder.toString();
    }

    private void renderHeader(LogMessage logMessage, LogStringBuilder builder) {
        builder.append(logMessage.getModule())
                .appendWithSeparator(logMessage.getEventName());

        if (logMessage.getResult() != null) {
            builder.appendWithSeparator(logMessage.getResult());
        }
    }

    private void renderSpendTime(LogMessage logMessage, LogStringBuilder builder) {
        long spendTime = logMessage.spendTime();
        if (spendTime >= logMessage.config().getMinSpendTimeMillsToDisplay()) {
            builder.appendWithSeparator(spendTime).append("ms");
        }
    }

    private void renderBody(LogMessage logMessage, LogStringBuilder builder) {
        for (Map.Entry<String, Object> entry : logMessage.fieldValues().entrySet()) {
            builder.appendWithSeparator(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }
    }

    private static class LogStringBuilder {

        StringBuilder stringBuilder;
        LogMessageConfig config;

        public LogStringBuilder(LogMessageConfig config) {
            this.stringBuilder = new StringBuilder();
            this.config = config;
        }

        private LogStringBuilder append(Object value) {
            stringBuilder.append(value);
            return this;
        }

        private LogStringBuilder separator() {
            stringBuilder.append(config.getSeparator());
            return this;
        }

        private LogStringBuilder appendWithSeparator(Object value) {
            return separator().append(value);
        }

        @Override
        public String toString() {
            return stringBuilder.toString();
        }
    }
}