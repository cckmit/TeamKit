package org.team4u.kit.core.error;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jay.wu
 */
public class ErrorCode {

    private static final Map<String, ErrorCode> ERROR_CODES = new ConcurrentHashMap<String, ErrorCode>();

    public static final ErrorCode SUCCESS = new ErrorCode("common", "0", "处理成功");

    public static final ErrorCode NOT_LOGIN = new ErrorCode("common", "not.login", "没有登录");
    public static final ErrorCode AUTH_ACCESS_DENIED = new ErrorCode("common", "auth.access.denied", "无权限访问");

    public static final ErrorCode SYSTEM_ERROR = new ErrorCode("common", "system.error", "系统异常");
    public static final ErrorCode DATA_NOT_FOUND = new ErrorCode("common", "not.found", "%s数据没有找到");

    public static final ErrorCode PARAM_EMPTY = new ErrorCode("common", "param.empty", "%s参数为空");
    public static final ErrorCode PARAM_ERROR = new ErrorCode("common", "param.error", "非法参数：%s");

    private final String type;
    private final String code;
    private final String message;
    private final String innerCode;

    public ErrorCode(String type, String code, String message) {
        this.type = type;
        this.code = code;
        this.message = message;
        this.innerCode = toInnerName(type, code);

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

    public String getInnerCode() {
        return innerCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageCode: " + innerCode;
    }

    private void register() {
        ERROR_CODES.put(innerCode, this);
    }
}