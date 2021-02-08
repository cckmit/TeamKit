package org.team4u.command.infrastructure.executor.camel;

import org.team4u.command.domain.executor.handler.log.CommandLogHandler;
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
                .to("bean:commandLogHandler?method=internalHandle")
                .to("bean:commandRequester?method=internalHandle")
                .to("bean:commandLogHandler?method=internalHandle");
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