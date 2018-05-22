package org.team4u.kit.core.log;


import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jay Wu
 */
public class LogMessage {

    private final static String RESULT_PROCESSING = "processing";
    private final static String RESULT_SUCCESS = "success";
    private final static String RESULT_FAIL = "fail";

    private final static String SEPARATOR = "|";

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
        result(RESULT_PROCESSING);
        return this;
    }

    /**
     * 处理成功
     */
    public LogMessage success() {
        result(RESULT_SUCCESS);
        return this;
    }

    /**
     * 处理失败
     */
    public LogMessage fail() {
        result(RESULT_FAIL);
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
        logMessage.fieldValues = new HashMap<String, Object>(fieldValues);
        return logMessage;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(module).append(SEPARATOR).append(eventName);

        if (result != null) {
            stringBuilder.append(SEPARATOR).append(result);
        }

        for (Map.Entry<String, Object> entry : fieldValues.entrySet()) {
            stringBuilder.append(SEPARATOR)
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }

        return stringBuilder.toString();
    }
}