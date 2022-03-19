package org.team4u.config.domain;

/**
 * 配置项资源库
 *
 * @author jay.wu
 */
public interface SimpleConfigRepository {

    /**
     * 获取所有配置项
     *
     * @return 配置项集合
     */
    SimpleConfigs allConfigs();
}