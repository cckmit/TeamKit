package org.team4u.id.domain.seq.value;

import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 序号值提供者服务
 *
 * @author jay.wu
 */
public class SequenceProviderFactoryHolder extends PolicyRegistrar<String, SequenceProvider.Factory<?>> {

    public SequenceProviderFactoryHolder() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    /**
     * 生成序号
     *
     * @param context 上下文
     * @return 序号
     */
    public Number provide(SequenceProvider.Context context) {
        return availablePolicyOf(context.getSequenceConfig().getSequenceConfigId())
                .create(context.getSequenceConfig().getSequenceConfig())
                .provide(context);
    }
}