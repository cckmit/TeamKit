package org.team4u.selector.application;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Func1;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.error.DataNotExistException;
import org.team4u.base.error.NestedException;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.log.LogMessage;
import org.team4u.base.log.LogMessageContext;
import org.team4u.selector.domain.interceptor.SelectorInterceptor;
import org.team4u.selector.domain.interceptor.SelectorInterceptorFactory;
import org.team4u.selector.domain.interceptor.SelectorInterceptorFactoryService;
import org.team4u.selector.domain.interceptor.SelectorInterceptorService;
import org.team4u.selector.domain.selector.*;
import org.team4u.selector.domain.selector.binding.SelectorBinding;

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

    private final SelectorInterceptorService selectorInterceptorService;
    private final SelectorInterceptorFactoryService selectorInterceptorFactoryService;

    public SelectorAppService(SelectorConfigRepository selectorConfigRepository) {
        this.selectorConfigRepository = selectorConfigRepository;
        this.selectorFactoryService = new SelectorFactoryService();

        this.selectorInterceptorService = new SelectorInterceptorService();
        this.selectorInterceptorFactoryService = new SelectorInterceptorFactoryService();
    }

    /**
     * 根据配置标识，选择最终结果
     *
     * @return 选择结果
     */
    public SelectorResult select(String selectorConfigId) {
        return select(selectorConfigId, null);
    }

    /**
     * 根据选择标识和绑定值，选择最终结果
     *
     * @return 选择结果
     */
    public SelectorResult select(String selectorConfigId, SelectorBinding binding) {
        LogMessageContext.createAndSet(this.getClass().getSimpleName(), "select")
                .append("id", selectorConfigId);
        return select(selectorConfigOfId(selectorConfigId), binding);
    }

    /**
     * 根据配置对象和绑定值、选择最终结果
     *
     * @return 选择结果
     */
    public SelectorResult select(SelectorConfig selectorConfig, SelectorBinding binding) {
        LogMessage lm = LogMessageContext.getOrCreate(this.getClass().getSimpleName(), "select");

        if (selectorConfig == null) {
            log.info(lm.fail("selectorConfig is null").toString());
            return SelectorResult.NOT_MATCH;
        }
        lm.append("id", selectorConfig.getConfigId()).append("binding", binding);

        try {
            SelectorResult result = withInterceptor(selectorConfig, binding, selectorOfConfig(selectorConfig)::select);

            if (log.isDebugEnabled()) {
                log.debug(lm.success().append("result", result).toString());
            }

            return result;
        } catch (Exception e) {
            log.error(e, lm.fail(e.getMessage()).toString());
            throw NestedException.wrap(e);
        }
    }

    private SelectorResult withInterceptor(SelectorConfig selectorConfig,
                                           SelectorBinding binding,
                                           Func1<SelectorBinding, SelectorResult> select) throws Exception {
        List<SelectorInterceptor> interceptors = interceptorsOfConfig(selectorConfig);
        SelectorBinding newBinding = selectorInterceptorService.preHandle(interceptors, binding);
        SelectorResult result = select.call(newBinding);
        return selectorInterceptorService.postHandle(interceptors, newBinding, result);
    }

    /**
     * 根据选择配置标识，判断是否命中
     */
    public boolean match(String selectorConfigId) {
        return match(selectorConfigId, null);
    }

    /**
     * 根据选择配置标识和绑定值，判断是否命中
     */
    public boolean match(String selectorConfigId, SelectorBinding binding) {
        return select(selectorConfigId, binding).isMatch();
    }

    /**
     * 注册选择器构建工厂
     */
    public SelectorAppService registerSelectorFactory(SelectorFactory factory) {
        selectorFactoryService.registerPolicy(factory);
        return this;
    }

    /**
     * 注册选择拦截器构建工厂
     */
    public SelectorAppService registerSelectorInterceptorFactory(SelectorInterceptorFactory factory) {
        selectorInterceptorFactoryService.registerPolicy(factory);
        return this;
    }

    /**
     * 获取选择器配置
     */
    private SelectorConfig selectorConfigOfId(String selectorConfigId) {
        return selectorConfigRepository.configOfId(selectorConfigId);
    }

    /**
     * 获取选择器
     */
    private Selector selectorOfConfig(SelectorConfig selectorConfig) throws DataNotExistException {
        SelectorFactory factory = selectorFactoryService.policyOf(selectorConfig.getType());

        if (factory == null) {
            throw new SystemDataNotExistException("Unable to find selector|id=" + selectorConfig.getConfigId());
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
                    SelectorInterceptorFactory interceptorFactory = selectorInterceptorFactoryService.policyOf(it.getId());
                    Assert.notNull(interceptorFactory, "InterceptorFactory is null|id=" + it.getId());

                    return interceptorFactory.create(it.getConfig());
                })
                .collect(Collectors.toList());
    }
}