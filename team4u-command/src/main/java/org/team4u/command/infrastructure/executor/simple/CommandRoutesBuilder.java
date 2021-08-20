package org.team4u.command.infrastructure.executor.simple;


import org.team4u.base.filter.Filter;
import org.team4u.base.registrar.StringIdPolicy;
import org.team4u.command.domain.executor.handler.CommandHandler;

import java.util.List;

/**
 * 命令路由构建器
 *
 * @author jay.wu
 */
public interface CommandRoutesBuilder extends StringIdPolicy {

    /**
     * 配置处理器路由
     *
     * @return 命令处理器集合
     */
    List<Filter<CommandHandler.Context>> configure();
}