package org.team4u.command.infrastructure.executor.simple;

import cn.hutool.core.collection.CollUtil;
import org.team4u.command.domain.executor.CommandHandler;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

import java.util.List;

/**
 * 抽象命令路由构建器
 *
 * @author jay.wu
 */
public abstract class AbstractCommandRoutesBuilder implements CommandRoutesBuilder {

    private final CommandRequester<?, ?> commandRequester;

    public AbstractCommandRoutesBuilder(CommandRequester<?, ?> commandRequester) {
        this.commandRequester = commandRequester;
    }

    @Override
    public List<CommandHandler> configure() {
        return CollUtil.newArrayList(commandRequester);
    }

    public CommandHandler getCommandRequester() {
        return commandRequester;
    }
}