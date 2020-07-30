package org.team4u.command.infrastructure.executor.simple;

import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.filter.FilterChain;
import org.team4u.base.filter.FilterChainFactory;
import org.team4u.base.lang.IdObjectService;
import org.team4u.command.domain.config.CommandConfig;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.CommandHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于责任链的简单命令执行器
 *
 * @author jay.wu
 */
public class SimpleCommandExecutor extends IdObjectService<String, CommandRoutesBuilder> implements CommandExecutor {

    private final Map<String, FilterChain<CommandHandler.Context>> filterChainMap = new HashMap<>();

    public SimpleCommandExecutor(List<CommandRoutesBuilder> objects) {
        super(objects);

        initFilterChains();
    }

    private void initFilterChains() {
        for (CommandRoutesBuilder builder : idObjects()) {
            FilterChain<CommandHandler.Context> filterChain = FilterChainFactory.buildFilterChain(builder.configure());
            filterChainMap.put(builder.id(), filterChain);
        }
    }

    @Override
    public Object execute(CommandConfig config, Object request) {
        FilterChain<CommandHandler.Context> filterChain = filterChainMap.get(config.getCommandId());
        if (filterChain == null) {
            throw new SystemDataNotExistException("commandId=" + config.getCommandId());
        }

        CommandHandler.Context handlerContext = new CommandHandler.Context(config, request);
        filterChain.doFilter(handlerContext);
        return handlerContext.getResponse();
    }
}
