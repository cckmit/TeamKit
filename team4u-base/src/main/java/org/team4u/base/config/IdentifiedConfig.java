package org.team4u.base.config;

/**
 * 具有标识的配置
 *
 * @author jay.wu
 */
public class IdentifiedConfig {

    private String configId;

    public String getConfigId() {
        return configId;
    }

    public IdentifiedConfig setConfigId(String configId) {
        this.configId = configId;
        return this;
    }
}