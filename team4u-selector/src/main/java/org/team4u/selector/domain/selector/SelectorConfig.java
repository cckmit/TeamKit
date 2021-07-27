package org.team4u.selector.domain.selector;

import cn.hutool.core.lang.Assert;
import org.team4u.base.config.IdentifiedConfig;
import org.team4u.selector.domain.interceptor.InterceptorConfig;

import java.util.List;

/**
 * 选择器配置
 *
 * @author jay.wu
 */
public class SelectorConfig extends IdentifiedConfig {

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