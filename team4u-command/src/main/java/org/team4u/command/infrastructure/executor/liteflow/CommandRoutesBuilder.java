package org.team4u.command.infrastructure.executor.liteflow;


import org.team4u.base.lang.IdObject;

import java.util.List;

/**
 * 命令路由构建器
 *
 * @author jay.wu
 */
public interface CommandRoutesBuilder extends IdObject<String> {

    /**
     * 注册节点组件
     */
    void registerNodes();

    /**
     * 配置处理器路由
     *
     * @return 命令处理器集合
     */
    List<String> configure();
}
