package org.team4u.command.infrastructure.executor.filter;

import org.team4u.base.filter.v2.Filter;
import org.team4u.command.domain.executor.handler.CommandHandler;

/**
 * 命令过滤器构建器
 *
 * @author jay.wu
 */
public class CommandFilterBuilder {

    /**
     * 根据命令处理器构建顺序过滤器
     *
     * @param commandHandler 命令处理器
     * @return 顺序过滤器
     */
    public static Filter<CommandHandler.Context> filter(CommandHandler commandHandler) {
        return context -> {
            commandHandler.handle(context);
            return true;
        };
    }
}