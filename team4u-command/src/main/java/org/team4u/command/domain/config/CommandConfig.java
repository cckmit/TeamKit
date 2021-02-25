package org.team4u.command.domain.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Dict;

import java.util.Map;

/**
 * 命令配置
 *
 * @author jay.wu
 */
public class CommandConfig {

    /**
     * 命令配置明细项
     * <p>
     * key = 类型
     * value = 配置集合
     */
    private final Map<String, Dict> items;
    /**
     * 配置标识
     */
    private String configId;

    public CommandConfig(Map<String, Dict> items) {
        this.items = items;
    }

    public String getConfigId() {
        return configId;
    }

    public CommandConfig setConfigId(String configId) {
        this.configId = configId;
        return this;
    }

    /**
     * 获取某个类型的配置
     *
     * @param itemId     配置标识
     * @param configType 配置类型
     * @param <T>        配置类型
     * @return 配置对象
     */
    public <T> T itemOf(String itemId, Class<T> configType) {
        return BeanUtil.toBean(itemOf(itemId), configType);
    }

    /**
     * 获取某个类型的配置
     *
     * @param itemId 配置标识
     * @return 配置对象
     */
    public Dict itemOf(String itemId) {
        return items.get(itemId);
    }
}