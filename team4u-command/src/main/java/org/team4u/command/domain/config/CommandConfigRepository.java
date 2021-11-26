package org.team4u.command.domain.config;

import java.util.Set;

/**
 * 命令配置
 *
 * @author jay.wu
 */
public interface CommandConfigRepository {

    /**
     * 获取命令配置
     *
     * @param id 命令标识
     * @return 命令配置
     */
    CommandConfig configOfId(String id);

    /**
     * 保存命令配置
     *
     * @param config 配置
     */
    void save(CommandConfig config);

    /**
     * 获取所有配置标识
     *
     * @return 配置标识集合
     */
    Set<String> allConfigIdList();
}