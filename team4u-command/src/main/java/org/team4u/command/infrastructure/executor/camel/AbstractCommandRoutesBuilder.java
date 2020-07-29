package org.team4u.command.infrastructure.executor.camel;

import org.apache.camel.builder.RouteBuilder;
import org.team4u.command.domain.executor.CommandHandler;
import org.team4u.command.domain.executor.handler.requester.CommandRequester;

public abstract class AbstractCommandRoutesBuilder extends RouteBuilder implements CommandRoutesBuilder {

    private final CommandRequester<?, ?> commandRequester;

    public AbstractCommandRoutesBuilder(CommandRequester<?, ?> commandRequester) {
        this.commandRequester = commandRequester;
    }


    @Override
    public void configure() {
        bindToRegistry();

        from(startUri())
                .to("bean:commandRequester");
    }

    protected void bindToRegistry() {
        bindToRegistry("commandRequester", commandRequester);
    }

    public CommandHandler getCommandRequester() {
        return commandRequester;
    }

    protected String startUri() {
        return "direct:" + id();
    }
}