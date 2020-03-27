package org.team4u.kit.core.log;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class LogMessage {

    private LogMessageConfig config = new LogMessageConfig();

    private Long processingTime = System.currentTimeMillis();
    private Long completedTime;

    private String module;
    private String eventName;
    private String result;

    private Map<String, Object> fieldValues = new LinkedHashMap<String, Object>();

    public LogMessage(String module, String eventName) {
        this.module = module;
        this.eventName = eventName;
    }

    public LogMessage(String eventName) {
        this(null, eventName);
    }

    public static LogMessage create(String module, String eventName) {
        return create(module, eventName, null);
    }

    public static LogMessage create(String module, String eventName, String result) {
        return new LogMessage(module, eventName).setResult(result);
    }

    public LogMessageConfig config() {
        return config;
    }

    public String getModule() {
        return module;
    }

    public LogMessage setModule(String module) {
        this.module = module;
        return this;
    }

    public String getEventName() {
        return eventName;
    }

    public LogMessage setEventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public String getResult() {
        return result;
    }

    public LogMessage setResult(String result) {
        this.result = result;
        return this;
    }

    public LogMessage append(String key, Object value) {
        fieldValues.put(key, value);
        return this;
    }

    public LogMessage setKeyValues(Map<String, Object> keyValues) {
        fieldValues.putAll(keyValues);
        return this;
    }

    /**
     * 处理中
     */
    public LogMessage processing() {
        result(config().getProcessingKey());
        processingTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 处理成功
     */
    public LogMessage success() {
        result(config().getSucceededKey());
        completedTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 处理失败
     */
    public LogMessage fail() {
        result(config().getFailedKey());
        completedTime = System.currentTimeMillis();
        return this;
    }

    /**
     * 处理失败
     */
    public LogMessage fail(String errorMessage) {
        append("errorMessage", errorMessage);
        return fail();
    }

    /**
     * 设置处理结果
     */
    public LogMessage result(String result) {
        this.result = result;
        return this;
    }

    public LogMessage copy() {
        LogMessage logMessage = new LogMessage(module, eventName);
        logMessage.result = result;
        logMessage.fieldValues = new HashMap<>(fieldValues);
        return logMessage;
    }

    public String toString() {
        return config().getLogMessageRender().render(this);
    }

    public long spendTime() {
        if (processingTime == null || completedTime == null) {
            return 0;
        }

        return completedTime - processingTime;
    }

    public Long processingTime() {
        return processingTime;
    }

    public Long completedTime() {
        return completedTime;
    }

    public Map<String, Object> fieldValues() {
        return fieldValues;
    }

    public LogMessage setProcessingTime(Long processingTime) {
        this.processingTime = processingTime;
        return this;
    }

    public LogMessage setCompletedTime(Long completedTime) {
        this.completedTime = completedTime;
        return this;
    }
}