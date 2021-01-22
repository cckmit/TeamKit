package org.team4u.selector.domain.interceptor;

import cn.hutool.core.lang.Dict;

/**
 * 拦截器配置
 *
 * @author jay.wu
 */
public class InterceptorConfig {
    /**
     * 拦截器工厂标识
     */
    private String id;
    /**
     * 拦截器配置值
     */
    private Dict config;

    public String getId() {
        return id;
    }

    public InterceptorConfig setId(String id) {
        this.id = id;
        return this;
    }

    public Dict getConfig() {
        return config;
    }

    public InterceptorConfig setConfig(Dict config) {
        this.config = config;
        return this;
    }
}
