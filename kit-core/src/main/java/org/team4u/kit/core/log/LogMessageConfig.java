package org.team4u.kit.core.log;

public class LogMessageConfig {

    private String separator = "|";

    private String processingKey = "processing";

    private String succeededKey = "succeeded";

    private String failedKey = "failed";

    private long minSpendTimeMillsToDisplay = 500;

    private LogMessageRender logMessageRender = new SimpleLogMessageRender();

    public String getSeparator() {
        return separator;
    }

    public LogMessageConfig setSeparator(String separator) {
        this.separator = separator;
        return this;
    }

    public String getProcessingKey() {
        return processingKey;
    }

    public LogMessageConfig setProcessingKey(String processingKey) {
        this.processingKey = processingKey;
        return this;
    }

    public String getSucceededKey() {
        return succeededKey;
    }

    public LogMessageConfig setSucceededKey(String succeededKey) {
        this.succeededKey = succeededKey;
        return this;
    }

    public String getFailedKey() {
        return failedKey;
    }

    public LogMessageConfig setFailedKey(String failedKey) {
        this.failedKey = failedKey;
        return this;
    }

    public long getMinSpendTimeMillsToDisplay() {
        return minSpendTimeMillsToDisplay;
    }

    public LogMessageConfig setMinSpendTimeMillsToDisplay(long minSpendTimeMillsToDisplay) {
        this.minSpendTimeMillsToDisplay = minSpendTimeMillsToDisplay;
        return this;
    }

    public LogMessageRender getLogMessageRender() {
        return logMessageRender;
    }

    public LogMessageConfig setLogMessageRender(LogMessageRender logMessageRender) {
        this.logMessageRender = logMessageRender;
        return this;
    }
}
