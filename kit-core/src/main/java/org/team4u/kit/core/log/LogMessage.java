package org.team4u.kit.core.log;

import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.URLUtil;

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
    private String method;
    private String result;

    private StringBuilder bodyBuilder = new StringBuilder();

    public LogMessage(String module, String method) {
        this.module = module;
        this.method = method;
    }

    public String getModule() {
        return module;
    }

    public LogMessage setModule(String module) {
        this.module = module;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public LogMessage setMethod(String method) {
        this.method = method;
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
        bodyBuilder.append(SEPARATOR)
                .append("\"").append(key).append("\"").append("=")
                .append("\"").append(value).append("\"");
        return this;
    }

    public LogMessage appendWithEscape(String key, Object value) {
        append(key, value == null ? null : URLUtil.encode(value.toString(), CharsetUtil.UTF_8));
        return this;
    }

    public LogMessage setKeyValues(Map<String, Object> keyValues) {
        for (Map.Entry<String, Object> kv : keyValues.entrySet()) {
            append(kv.getKey(), kv.getValue());
        }
        return this;
    }

    public LogMessage processing() {
        result = RESULT_PROCESSING;
        return this;
    }

    public LogMessage success() {
        result = RESULT_SUCCESS;
        return this;
    }

    public LogMessage fail() {
        result = RESULT_FAIL;
        return this;
    }

    public LogMessage fail(String errorMessage) {
        append("errorMessage", errorMessage);
        return fail();
    }

    public LogMessage copy() {
        LogMessage logMessage = new LogMessage(module, method);
        logMessage.result = result;
        logMessage.bodyBuilder = new StringBuilder(bodyBuilder.toString());
        return logMessage;
    }

    public String toString() {
        StringBuilder headerBuilder = new StringBuilder();
        headerBuilder.append(module).append(SEPARATOR).append(method);

        if (result != null) {
            headerBuilder.append(SEPARATOR).append(result);
        }
        headerBuilder.append(bodyBuilder.toString());

        return headerBuilder.toString();
    }
}