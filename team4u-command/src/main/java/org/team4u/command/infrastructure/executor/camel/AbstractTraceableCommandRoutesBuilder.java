package org.team4u.command.infrastructure.executor.camel;

import org.team4u.command.domain.executor.handler.CommandLogHandler;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

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
    public void configure() {
        bindToRegistry();

        from(startUri())
                .to("bean:commandLogHandler?method=handle")
                .to("bean:commandRequester?method=handle")
                .to("bean:commandLogHandler?method=handle");
    }

    @Override
    protected void bindToRegistry() {
        super.bindToRegistry();
        bindToRegistry("commandLogHandler", commandLogHandler);
    }

    public CommandLogHandler getCommandLogHandler() {
        return commandLogHandler;
    }
}