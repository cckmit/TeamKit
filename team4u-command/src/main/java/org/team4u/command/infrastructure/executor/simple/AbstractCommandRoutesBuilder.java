package org.team4u.command.infrastructure.executor.simple;

import cn.hutool.core.collection.CollUtil;
import org.team4u.base.filter.Filter;
import org.team4u.base.filter.FilterBuilder;
import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

import java.util.List;

/**
 * 抽象命令路由构建器
 *
 * @author jay.wu
 */
public abstract class AbstractCommandRoutesBuilder implements CommandRoutesBuilder {

    private final Filter<CommandHandler.Context> commandRequester;

    public AbstractCommandRoutesBuilder(CommandRequester<?, ?> commandRequester) {
        this.commandRequester = new FilterBuilder<CommandHandler.Context>().setWorker(commandRequester::handle).build();
    }

    @Override
    public List<Filter<CommandHandler.Context>> configure() {
        return CollUtil.newArrayList(commandRequester);
    }

    public Filter<CommandHandler.Context> getCommandRequester() {
        return commandRequester;
    }
}