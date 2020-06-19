package org.team4u.command.handler.extract;

import org.team4u.command.handler.HandlerConfig;

/**
 * 提取配置
 *
 * @author jay.wu
 */
public class ExtractConfig extends HandlerConfig {

    /**
     * 提取模板
     */
    private String template;
    /**
     * 目标类型
     */
    private String targetType;

    public String getTemplate() {
        return template;
    }

    public ExtractConfig setTemplate(String template) {
        this.template = template;
        return this;
    }

    public String getTargetType() {
        return targetType;
    }

    public ExtractConfig setTargetType(String targetType) {
        this.targetType = targetType;
        return this;
    }
}