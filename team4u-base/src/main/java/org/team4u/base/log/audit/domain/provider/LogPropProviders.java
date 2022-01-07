package org.team4u.base.log.audit.domain.provider;

import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 日志属性提供者服务
 *
 * @author jay.wu
 */
public class LogPropProviders extends PolicyRegistrar<Class<? extends LogPropProvider>, LogPropProvider> {

    public LogPropProviders() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    @SuppressWarnings("unchecked")
    public <T extends LogPropProvider> T getBean(Class<T> type) {
        return (T) policyOf(type);
    }

    public IpProvider getIpProvider() {
        return getBean(IpProvider.class);
    }

    public ExtProvider getExtProvider() {
        return getBean(ExtProvider.class);
    }

    public ModuleProvider getModuleProvider() {
        return getBean(ModuleProvider.class);
    }

    public ActionProvider getActionProvider() {
        return getBean(ActionProvider.class);
    }

    public ResultProvider getResultProvider() {
        return getBean(ResultProvider.class);
    }

    public OperatorProvider getOperatorProvider() {
        return getBean(OperatorProvider.class);
    }

    public ReferenceIdProvider getReferenceIdProvider() {
        return getBean(ReferenceIdProvider.class);
    }

    public DescriptionProvider getDescriptionProvider() {
        return getBean(DescriptionProvider.class);
    }

    public SystemIdProvider getSystemIdProvider() {
        return getBean(SystemIdProvider.class);
    }
}