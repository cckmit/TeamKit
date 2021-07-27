package org.team4u.base.config;

/**
 * 具有标识的配置
 *
 * @author jay.wu
 */
public class IdentifiedConfig {

    private String id;

    public String getId() {
        return id;
    }

    public IdentifiedConfig setId(String id) {
        this.id = id;
        return this;
    }
}