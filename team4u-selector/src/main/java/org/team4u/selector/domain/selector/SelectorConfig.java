package org.team4u.selector.domain.selector;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.config.IdentifiedConfig;
import org.team4u.selector.domain.interceptor.InterceptorConfig;

import java.util.List;

/**
 * 选择器配置
 *
 * @author jay.wu
 */
@EqualsAndHashCode(callSuper = true)
@Data
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
}