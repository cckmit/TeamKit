package org.team4u.command.infrastructure.executor.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultCamelContext;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.error.SystemException;
import org.team4u.base.registrar.PolicyRegistrar;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Camel的命令执行器
 *
 * @author jay.wu
 */
public class CamelCommandExecutor extends PolicyRegistrar<String, CommandRoutesBuilder>
        implements CommandExecutor {

    private final Map<String, CamelContext> contexts = new HashMap<>();

    public CamelCommandExecutor() {
        start();
    }

    public CamelCommandExecutor(List<CommandRoutesBuilder> builders) {
        super(builders);
        start();
    }

    private void start() {
        for (CommandRoutesBuilder builder : policies()) {
            start(builder);
        }
    }

    public void saveAndStart(CommandRoutesBuilder builder) {
        registerPolicy(builder);
        start(builder);
    }

    private void start(CommandRoutesBuilder builder) {
        try {
            CamelContext context = new DefaultCamelContext();
            builder.addRoutesToCamelContext(context);
            context.start();

            contexts.put(builder.id(), context);
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    @Override
    public Object execute(CommandHandler.Context context) {
        CamelContext camelContext = contexts.get(context.getCommandId());
        if (camelContext == null) {
            throw new SystemDataNotExistException("commandId=" + context.getCommandId());
        }

        camelContext.createProducerTemplate()
                .sendBody(
                        "direct:" + context.getCommandId(),
                        ExchangePattern.InOut,
                        context
                );

        return context.getResponse();
    }
}