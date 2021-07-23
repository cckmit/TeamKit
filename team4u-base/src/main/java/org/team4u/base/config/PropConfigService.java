package org.team4u.base.config;

import cn.hutool.core.util.StrUtil;

import java.util.Properties;

public class PropConfigService implements ConfigService {

    private final String prefix;

    private final Properties properties = new Properties();

    public PropConfigService() {
        this(null);
    }

    public PropConfigService(String prefix) {
        this.prefix = standardizedPrefix(prefix);
    }

    @Override
    public String get(String key) {
        return properties.getProperty(prefix + key);
    }

    public Properties getProperties() {
        return properties;
    }

    private String standardizedPrefix(String prefix) {
        if (StrUtil.isBlank(prefix)) {
            return "";
        } else {
            return prefix.endsWith(".") ? prefix.substring(0, prefix.length() - 1) : prefix;
        }
    }
}