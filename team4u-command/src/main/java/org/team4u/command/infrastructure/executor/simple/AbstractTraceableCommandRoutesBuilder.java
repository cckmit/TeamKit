package org.team4u.command.infrastructure.executor.simple;

import cn.hutool.core.collection.CollUtil;
import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.command.domain.executor.handler.log.CommandLogHandler;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

import java.util.List;

/**
 * 抽象可追踪的命令路由构建器
 *
 * @author jay.wu
 */
public abstract class AbstractTraceableCommandRoutesBuilder extends AbstractCommandRoutesBuilder {

    private final CommandLogHandler commandLogHandler;

    public AbstractTraceableCommandRoutesBuilder(CommandRequester<?, ?> commandRequester,
                                                 CommandLogHandler commandLogHandler) {
        super(commandRequester);

        this.commandLogHandler = commandLogHandler;
    }

    @Override
    public List<CommandHandler> configure() {
        return CollUtil.newArrayList(
                commandLogHandler,
                getCommandRequester(),
                commandLogHandler
        );
    }

    public CommandLogHandler getCommandLogHandler() {
        return commandLogHandler;
    }
}