package org.team4u.base.config;

import lombok.Data;

/**
 * 具有标识的配置
 *
 * @author jay.wu
 */
@Data
public class IdentifiedConfig {
    /**
     * 配置标识
     */
    private String configId;
}