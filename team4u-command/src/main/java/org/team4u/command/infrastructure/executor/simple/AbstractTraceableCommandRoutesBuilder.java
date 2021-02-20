package org.team4u.command.infrastructure.executor.simple;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.filter.Filter;
import org.team4u.base.filter.FilterBuilder;
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

    private final Filter<CommandHandler.Context> commandLogHandler;

    public AbstractTraceableCommandRoutesBuilder(CommandRequester<?, ?> commandRequester,
                                                 CommandLogHandler commandLogHandler) {
        super(commandRequester);

        this.commandLogHandler = new FilterBuilder<CommandHandler.Context>().setWorker(commandLogHandler::handle).build();

    }

    @Override
    public List<Filter<CommandHandler.Context>> configure() {
        return CollUtil.newArrayList(
                getCommandLogHandler(),
                getCommandRequester(),
                getCommandLogHandler()
        );
    }

    public Filter<CommandHandler.Context> getCommandLogHandler() {
        return commandLogHandler;
    }
}