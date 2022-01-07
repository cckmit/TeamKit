package org.team4u.base.log.audit.domain;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 审计日志上下文
 *
 * @author jay.wu
 */
@Data
@Builder
public class AuditLogContext {

    private static final ThreadLocal<Map<String, Object>> extThreadLocal = ThreadLocal.withInitial(HashMap::new);

    private Method method;
    private Object[] methodArgs;
    private Object returnValue;
    private Throwable error;

    private String referenceId;
    private String moduleId;
    private Date occurredOn;
    private String actionId;
    private String description;

    /**
     * 设置扩展信息
     *
     * @param ext 扩展Map
     */
    public static void setExt(Map<String, Object> ext) {
        extThreadLocal.set(ext);
    }

    /**
     * 设置扩展信息
     *
     * @param key   键
     * @param value 值
     */
    public static void setExt(String key, Object value) {
        extThreadLocal.get().put(key, value);
    }

    /**
     * 获取扩展信息
     *
     * @return 扩展Map
     */
    public static Map<String, Object> ext() {
        return extThreadLocal.get();
    }
}