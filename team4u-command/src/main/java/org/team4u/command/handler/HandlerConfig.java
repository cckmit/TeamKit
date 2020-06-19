package org.team4u.command.handler;

/**
 * 处理器配置
 *
 * @author jay.wu
 */
public class HandlerConfig {

    /**
     * 来源key
     */
    private String sourceKey;
    /**
     * 目标key
     */
    private String targetKey;

    public String getSourceKey() {
        return sourceKey;
    }

    public HandlerConfig setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
        return this;
    }

    public String getTargetKey() {
        return targetKey;
    }

    public HandlerConfig setTargetKey(String targetKey) {
        this.targetKey = targetKey;
        return this;
    }
}