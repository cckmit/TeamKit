package org.team4u.command;

import cn.hutool.core.collection.CollUtil;
import org.team4u.command.config.CommandConfig;
import org.team4u.command.config.CommandConfigRepository;
import org.team4u.base.filter.FilterChain;
import org.team4u.base.filter.FilterChainFactory;
import org.team4u.base.lang.EasyMap;
import org.team4u.base.lang.IdObjectService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理器服务
 *
 * @author jay.wu
 */
public class HandlerService extends IdObjectService<String, Handler> {

    private final CommandConfigRepository commandConfigRepository;

    public HandlerService(List<? extends Handler> objects,
                          CommandConfigRepository commandConfigRepository) {
        super(new ArrayList<>(objects));
        this.commandConfigRepository = commandConfigRepository;
    }

    public EasyMap invoke(String configId, EasyMap bindings) {
        CommandConfig config = commandConfigRepository.objectOf(configId);
        List<Handler> handlers = handlersOfId(config);

        EasyMap attributes = new EasyMap(bindings);
        if (handlers.isEmpty()) {
            return attributes;
        }

        invokeHandlerChain(config, handlers, attributes);

        return attributes;
    }

    private void invokeHandlerChain(CommandConfig config,
                                    List<Handler> handlers,
                                    EasyMap attributes) {
        FilterChain<Handler.Context> handlerChain = FilterChainFactory.buildFilterChain(handlers);
        handlerChain.doFilter(new Handler.Context(attributes, config));
    }

    private List<Handler> handlersOfId(CommandConfig config) {
        return config.getHandlers()
                .stream()
                .map(it -> CollUtil.getFirst(it.keySet()))
                .map(this::availableObjectOfId)
                .collect(Collectors.toList());
    }
}