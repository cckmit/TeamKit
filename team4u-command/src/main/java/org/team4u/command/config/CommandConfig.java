package org.team4u.command.config;


import org.team4u.core.lang.EasyMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 命令配置
 *
 * @author jay.wu
 */
public class CommandConfig {

    /**
     * 处理器描述集合
     */
    private List<EasyMap> handlers = new ArrayList<>();

    /**
     * 拦截器标识集合
     */
    private List<String> interceptors = Collections.emptyList();

    public EasyMap configOf(String handlerId) {
        EasyMap handlerConfigMap = handlers.
                stream()
                .filter(it -> it.containsKey(handlerId))
                .findFirst()
                .orElse(null);

        if (handlerConfigMap == null) {
            return new EasyMap();
        }

        return handlerConfigMap.getProperty(handlerId, EasyMap.class);
    }

    public List<EasyMap> getHandlers() {
        return handlers;
    }

    public CommandConfig setHandlers(List<EasyMap> handlers) {
        this.handlers = handlers;
        return this;
    }

    public List<String> getInterceptors() {
        return interceptors;
    }

    public CommandConfig setInterceptors(List<String> interceptors) {
        this.interceptors = interceptors;
        return this;
    }
}
