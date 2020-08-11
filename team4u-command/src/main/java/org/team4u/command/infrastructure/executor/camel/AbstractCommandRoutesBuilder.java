package org.team4u.command.infrastructure.executor.camel;

import org.apache.camel.builder.RouteBuilder;
import org.team4u.command.domain.executor.handler.CommandHandler;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

/**
 * 抽象命令路由构建器
 *
 * @author jay.wu
 */
public abstract class AbstractCommandRoutesBuilder extends RouteBuilder implements CommandRoutesBuilder {

    private final CommandRequester<?, ?> commandRequester;

    public AbstractCommandRoutesBuilder(CommandRequester<?, ?> commandRequester) {
        this.commandRequester = commandRequester;
    }

    @Override
    public void configure() {
        bindToRegistry();

        from(startUri()).to("bean:commandRequester?method=handle");
    }

    public CommandHandler getCommandRequester() {
        return commandRequester;
    }

    protected void bindToRegistry() {
        bindToRegistry("commandRequester", commandRequester);
    }

    protected String startUri() {
        return "direct:" + id();
    }
}