package org.team4u.selector.application;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.selector.domain.config.entity.SelectorConfig;
import org.team4u.selector.domain.config.repository.SelectorConfigRepository;
import org.team4u.selector.domain.interceptor.entity.SelectorInterceptor;
import org.team4u.selector.domain.interceptor.entity.SelectorInterceptorFactory;
import org.team4u.selector.domain.interceptor.service.SelectorInterceptorFactoryService;
import org.team4u.selector.domain.interceptor.service.SelectorInterceptorService;
import org.team4u.selector.domain.selector.entity.Selector;
import org.team4u.selector.domain.selector.entity.SelectorFactory;
import org.team4u.selector.domain.selector.entity.binding.SelectorBinding;
import org.team4u.selector.domain.selector.service.SelectorFactoryService;
import org.team4u.base.log.LogMessage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 选择器应用服务
 *
 * @author jay.wu
 */
public class SelectorAppService {

    private final Log log = LogFactory.get();

    private final SelectorConfigRepository selectorConfigRepository;
    private final SelectorFactoryService selectorFactoryService;
    private final SelectorInterceptorFactoryService selectorInterceptorFactoryService;
    private final SelectorInterceptorService selectorInterceptorService;

    public SelectorAppService(SelectorConfigRepository selectorConfigRepository) {
        this.selectorConfigRepository = selectorConfigRepository;

        this.selectorInterceptorFactoryService = new SelectorInterceptorFactoryService();
        this.selectorFactoryService = new SelectorFactoryService();
        this.selectorInterceptorService = new SelectorInterceptorService();
    }

    /**
     * 根据选择标识选择最终结果
     */
    public String select(String selectorConfigId) {
        return select(selectorConfigId, null);
    }

    /**
     * 根据选择标识、绑定值最终结果
     */
    public String select(String selectorConfigId, SelectorBinding binding) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "select")
                .append("selectorConfigId", selectorConfigId);

        // 获取选择器配置
        SelectorConfig selectorConfig = selectorConfigOfId(selectorConfigId);
        if (selectorConfig == null) {
            log.warn(lm.fail("selectorConfig is null").toString());
            return Selector.NONE;
        }

        // 获取拦截器集合
        List<SelectorInterceptor> interceptors = interceptorsOfConfig(selectorConfig);

        // 处理前置拦截
        SelectorBinding newBinding = selectorInterceptorService.preHandle(interceptors, binding);

        // 执行选择
        Selector selector = selectorOfConfig(selectorConfig);
        if (selector == null) {
            log.warn(lm.fail("selector is null").toString());
            return Selector.NONE;
        }

        String value = selector.select(newBinding);
        // 处理后置拦截
        return selectorInterceptorService.postHandle(interceptors, newBinding, value);
    }

    /**
     * 根据选择标识判断是否命中
     */
    public boolean match(String selectorConfigId) {
        return match(selectorConfigId, null);
    }

    /**
     * 根据选择标识、绑定值判断是否命中
     */
    public boolean match(String selectorConfigId, SelectorBinding binding) {
        return !StrUtil.equals(select(selectorConfigId, binding), Selector.NONE);
    }

    /**
     * 注册选择器构建工厂
     */
    public SelectorAppService registerSelectorFactory(SelectorFactory factory) {
        selectorFactoryService.saveIdObject(factory);
        return this;
    }

    /**
     * 注册选择拦截器构建工厂
     */
    public SelectorAppService registerSelectorInterceptorFactory(SelectorInterceptorFactory factory) {
        selectorInterceptorFactoryService.saveIdObject(factory);
        return this;
    }

    /**
     * 获取选择器配置
     */
    private SelectorConfig selectorConfigOfId(String selectorConfigId) {
        return selectorConfigRepository.selectorConfigOfId(selectorConfigId);
    }

    /**
     * 获取选择器
     */
    private Selector selectorOfConfig(SelectorConfig selectorConfig) {
        SelectorFactory factory = selectorFactoryService.objectOfId(selectorConfig.getType());

        if (factory == null) {
            return null;
        }

        return factory.create(selectorConfig.getBody());
    }

    /**
     * 获取选择拦截器集合
     */
    private List<SelectorInterceptor> interceptorsOfConfig(SelectorConfig selectorConfig) {
        if (selectorConfig.getInterceptors() == null) {
            return Collections.emptyList();
        }

        return selectorConfig.getInterceptors()
                .stream()
                .map(it -> {
                    SelectorInterceptorFactory interceptorFactory = selectorInterceptorFactoryService.objectOfId(it.getId());
                    Assert.notNull(interceptorFactory, "InterceptorFactory is null|id=" + it.getId());

                    return interceptorFactory.create(it.getConfig());
                })
                .collect(Collectors.toList());
    }
}