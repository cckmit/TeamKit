package org.team4u.kit.core.error;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jay.wu
 */
public class ErrorCode {

    public static final ErrorCode NOT_LOGIN = new ErrorCode("common", "not.login", "没有登录");
    public static final ErrorCode AUTH_ACCESS_DENIED = new ErrorCode("common", "auth.access.denied", "无权限访问");
    public static final ErrorCode DATA_NOT_FOUND = new ErrorCode("common", "not.found", "%s数据没有找到");
    public static final ErrorCode PARAM_EMPTY = new ErrorCode("common", "param.empty", "%s参数为空");
    public static final ErrorCode PARAM_ERROR = new ErrorCode("common", "param.error", "非法参数：%s");
    public static final ErrorCode PARAM_ERROR_TIME_START = new ErrorCode("common", "param.error.time.start", "结束时间不能小于开始时间");
    public static final ErrorCode PARAM_ERROR_TIME_OVERFLOW = new ErrorCode("common", "param.error.time.overflow", "查询时间跨度不能超过 %s 天");
    private static final Map<String, ErrorCode> ERROR_CODES = new ConcurrentHashMap<String, ErrorCode>();
    private final String type;
    private final String code;
    private final String message;
    private final String innerName;

    public ErrorCode(String type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.innerName = toInnerName(type, code);

        register();
    }

    public static ErrorCode from(String type, String name) {
        return from(toInnerName(type, name));
    }

    public static ErrorCode from(String fullName) {
        return ERROR_CODES.get(fullName);
    }

    public static String toInnerName(String type, String name) {
        return type + '.' + name;
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getInnerName() {
        return innerName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageCode: " + innerName;
    }

    private void register() {
        ERROR_CODES.put(this.innerName, this);
    }
}