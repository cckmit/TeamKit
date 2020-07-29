package org.team4u.command.infrastructure.executor.camel;

import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.CommandHandler;
import org.team4u.command.domain.executor.CommandRequest;
import org.team4u.command.domain.executor.CommandResponse;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.error.SystemException;
import org.team4u.base.lang.IdObjectService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CamelCommandExecutor extends IdObjectService<String, CommandRoutesBuilder>
        implements CommandExecutor {

    private final Map<String, CamelContext> contexts = new HashMap<>();

    public CamelCommandExecutor(List<CommandRoutesBuilder> builders) {
        super(builders);

        start();
    }

    private void start() {
        try {
            for (CommandRoutesBuilder builder : idObjects()) {
                CamelContext context = new DefaultCamelContext();
                builder.addRoutesToCamelContext(context);
                context.start();

                contexts.put(builder.id(), context);
            }
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    @Override
    public CommandResponse execute(CommandConfig config, CommandRequest request) {
        CamelContext camelContext = contexts.get(request.getCommandId());
        if (camelContext == null) {
            throw new SystemDataNotExistException("commandId=" + request.getCommandId());
        }

        CommandHandler.Context handlerContext = new CommandHandler.Context(config, request);
        camelContext
                .createProducerTemplate()
                .sendBody(
                        "direct:" + request.getCommandId(),
                        ExchangePattern.InOut,
                        handlerContext
                );

        return handlerContext.getResponse();
    }
}