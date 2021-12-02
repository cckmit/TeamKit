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
     * @return 分组标识
     */
    public String provide(SequenceGroupKeyProvider.Context context) {
        SequenceGroupKeyProvider.Factory<?> factory = policyOf(context.getSequenceConfig().getGroupKeyConfigId());

        if (factory == null) {
            return "";
        }

        return factory.create(context.getSequenceConfig().getGroupKeyConfig())
                .provide(context);
    }
}