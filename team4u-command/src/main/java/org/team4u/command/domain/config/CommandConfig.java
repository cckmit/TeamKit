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
     * 命令标识
     */
    public final String commandId;

    /**
     * 命令配置明细项
     * <p>
     * key = 类型
     * value = 配置集合
     */
    private final Map<String, Dict> items;

    public CommandConfig(String commandId, Map<String, Dict> items) {
        this.commandId = commandId;
        this.items = items;
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
        return BeanUtil.toBean(items.get(itemId), configType);
    }

    public String getCommandId() {
        return commandId;
    }
}