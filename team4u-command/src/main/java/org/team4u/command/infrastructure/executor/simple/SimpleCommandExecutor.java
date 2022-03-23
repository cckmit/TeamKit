package org.team4u.command.infrastructure.executor.simple;

import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.filter.FilterChain;
import org.team4u.base.registrar.PolicyRegistrar;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于责任链的简单命令执行器
 * <p>
 * Deprecated, use FilterCommandExecutor instead
 *
 * @author jay.wu
 * @see org.team4u.command.infrastructure.executor.filter.FilterCommandExecutor
 */
@Deprecated
public class SimpleCommandExecutor
        extends PolicyRegistrar<String, CommandRoutesBuilder>
        implements CommandExecutor {

    private final Map<String, FilterChain<CommandHandler.Context>> filterChainMap = new HashMap<>();

    public SimpleCommandExecutor() {
        initFilterChains();
    }

    public SimpleCommandExecutor(List<CommandRoutesBuilder> objects) {
        super(objects);

        initFilterChains();
    }

    private void initFilterChains() {
        for (CommandRoutesBuilder builder : policies()) {
            initFilterChain(builder);
        }
    }

    public void saveAndInitFilterChain(CommandRoutesBuilder builder) {
        registerPolicy(builder);
        initFilterChain(builder);
    }

    private void initFilterChain(CommandRoutesBuilder builder) {
        FilterChain<CommandHandler.Context> filterChain = FilterChain.create(builder.configure());
        filterChainMap.put(builder.id(), filterChain);
    }

    @Override
    public void execute(CommandHandler.Context context) {
        FilterChain<CommandHandler.Context> filterChain = filterChainMap.get(context.getCommandId());
        if (filterChain == null) {
            throw new SystemDataNotExistException("commandId=" + context.getCommandId());
        }

        filterChain.doFilter(context);
    }
}