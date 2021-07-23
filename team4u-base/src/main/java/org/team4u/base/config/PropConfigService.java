package org.team4u.base.config;

import org.team4u.base.util.PathUtil;

import java.util.Properties;

/**
 * 基于配置文件的配置服务
 *
 * @author jay.wu
 */
public class PropConfigService implements ConfigService {

    private final String prefix;

    private Properties properties = new Properties();

    public PropConfigService() {
        this(null);
    }

    public PropConfigService(String prefix) {
        this.prefix = PathUtil.standardizedPrefix(".", prefix);
    }

    @Override
    public String get(String key) {
        return properties.getProperty(prefix + key);
    }

    public Properties getProperties() {
        return properties;
    }

    public PropConfigService setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }
}