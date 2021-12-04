package org.team4u.id.domain.seq.group;

import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 序号分组提供者服务
 *
 * @author jay.wu
 */
public class SequenceGroupKeyFactoryHolder extends PolicyRegistrar<String, SequenceGroupKeyProvider.Factory<?>> {

    public SequenceGroupKeyFactoryHolder() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    /**
     * 生成分组标识
     *
     * @param context 上下文
     * @return 分组标识，当无法找到工厂时，将返回空字符串
     */
    public String provide(SequenceGroupKeyProvider.Context context) {
        SequenceGroupKeyProvider provider = crete(
                context.getSequenceConfig().getGroupFactoryId(),
                context.getSequenceConfig().getGroupConfig()
        );

        if (provider == null) {
            return "";
        }

        return provider.provide(context);
    }

    /**
     * 创建分组提供者
     *
     * @param factoryId 工厂标识
     * @param config    工厂配置
     * @return 分组提供者
     */
    public SequenceGroupKeyProvider crete(String factoryId, String config) {
        SequenceGroupKeyProvider.Factory<?> factory = policyOf(factoryId);

        if (factory == null) {
            return null;
        }

        return factory.create(config);
    }
}