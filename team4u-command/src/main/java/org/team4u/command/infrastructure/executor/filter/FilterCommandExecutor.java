package org.team4u.command.infrastructure.executor.filter;

import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.filter.v2.Filter;
import org.team4u.base.filter.v2.FilterChain;
import org.team4u.base.registrar.PolicyRegistrar;
import org.team4u.command.domain.executor.CommandExecutor;
import org.team4u.command.domain.executor.handler.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 基于责任链的命令执行器
 *
 * @author jay.wu
 */
public class FilterCommandExecutor extends PolicyRegistrar<String, CommandRoutesBuilder> implements CommandExecutor {

    private final Map<String, FilterChain<CommandHandler.Context, ? extends Filter<CommandHandler.Context>>> filterChainMap = new HashMap<>();

    public FilterCommandExecutor() {
        initFilterChains();
    }

    public FilterCommandExecutor(List<CommandRoutesBuilder> builders) {
        super(builders);

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
        FilterChain.Config config = builder.configure();
        config.setNameIfNotDefault(builder.getClass().getSimpleName());
        filterChainMap.put(builder.id(), FilterChain.create(config));
    }

    @Override
    public void execute(CommandHandler.Context context) {
        Optional.ofNullable(filterChainMap.get(context.getCommandId()))
                .map(it -> it.doFilter(context))
                .orElseThrow(() -> new SystemDataNotExistException("commandId=" + context.getCommandId()));
    }
}