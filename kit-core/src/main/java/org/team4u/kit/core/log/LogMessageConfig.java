package org.team4u.kit.core.log;

/**
 * 日志消息配置
 *
 * @author jay.wu
 */
public class LogMessageConfig {

    /**
     * 日志消息分隔符
     */
    private String separator = "|";

    /**
     * 处理中关键字
     */
    private String processingKey = "processing";

    /**
     * 处理成功关键字
     */
    private String succeededKey = "succeeded";

    /**
     * 处理失败功关键字
     */
    private String failedKey = "failed";

    /**
     * 最小显示耗时（毫秒）阈值
     */
    private long minSpendTimeMillsToDisplay = 200;

    /**
     * 日志消息渲染器
     */
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