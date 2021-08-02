package org.team4u.base.config;

import cn.hutool.core.util.StrUtil;

import java.util.Properties;

/**
 * 基于配置文件的配置服务
 *
 * @author jay.wu
 */
public class PropConfigService implements ConfigService {

    private final static String JOINER = ".";

    private final String prefix;

    private Properties properties = new Properties();

    public PropConfigService() {
        this(null);
    }

    public PropConfigService(String prefix) {
        this.prefix = standardizedPrefix(prefix);
    }

    @Override
    public String get(String key) {
        return getProperties().getProperty(prefix + key);
    }

    public Properties getProperties() {
        return properties;
    }

    public PropConfigService setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    private String standardizedPrefix(String prefix) {
        if (StrUtil.isBlank(prefix)) {
            return "";
        }

        return prefix.endsWith(JOINER) ? prefix : (prefix + JOINER);
    }
}