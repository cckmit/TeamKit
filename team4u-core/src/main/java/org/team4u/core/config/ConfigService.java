package org.team4u.core.config;

/**
 * 配置服务
 *
 * @author jay.wu
 */
public interface ConfigService {

    /**
     * 获取配置值
     */
    String get(String key);

    /**
     * 获取配置值
     *
     * @param defaultValue 默认值，不能为null
     */
    <T> T get(String key, T defaultValue);
}