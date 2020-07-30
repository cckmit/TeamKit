package org.team4u.command.infrastructure.executor.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.error.SystemException;
import org.team4u.base.lang.IdObjectService;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Camel的命令执行器
 *
 * @author jay.wu
 */
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
    public Object execute(String commandId, CommandConfig config, Object request) {
        CamelContext camelContext = contexts.get(commandId);
        if (camelContext == null) {
            throw new SystemDataNotExistException("commandId=" + commandId);
        }

        CommandHandler.Context handlerContext = new CommandHandler.Context(commandId, config, request);

        camelContext.createProducerTemplate()
                .sendBody(
                        "direct:" + commandId,
                        ExchangePattern.InOut,
                        handlerContext
                );

        return handlerContext.getResponse();
    }
}