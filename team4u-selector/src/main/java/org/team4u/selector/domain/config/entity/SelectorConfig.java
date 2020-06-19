package org.team4u.selector.domain.config.entity;

import cn.hutool.core.lang.Assert;

import java.util.List;

/**
 * 选择器配置
 *
 * @author jay.wu
 */
public class SelectorConfig {

    /**
     * 选择器标识
     */
    public String id;

    /**
     * 选择器类型
     */
    private String type;

    /**
     * 选择器配置内容
     */
    private String body;
    /**
     * 拦截器集合
     */
    private List<InterceptorConfig> interceptors;

    public String getId() {
        return id;
    }

    public SelectorConfig setId(String id) {
        Assert.notNull(id, "Selector id is null");
        this.id = id;
        return this;
    }

    public String getType() {
        return type;
    }

    public SelectorConfig setType(String type) {
        Assert.notNull(type, "Selector type is null");
        this.type = type;
        return this;
    }

    public String getBody() {
        return body;
    }

    public SelectorConfig setBody(String body) {
        Assert.notNull(body, "Selector body is null");
        this.body = body;
        return this;
    }

    public List<InterceptorConfig> getInterceptors() {
        return interceptors;
    }

    public SelectorConfig setInterceptors(List<InterceptorConfig> interceptors) {
        this.interceptors = interceptors;
        return this;
    }
}