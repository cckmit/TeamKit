package org.team4u.command.infrastructure.executor.camel;

import org.team4u.command.domain.executor.handler.CommandLogHandler;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

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
                .to("bean:commandLogHandler")
                .to("bean:commandRequester")
                .to("bean:commandLogHandler");
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