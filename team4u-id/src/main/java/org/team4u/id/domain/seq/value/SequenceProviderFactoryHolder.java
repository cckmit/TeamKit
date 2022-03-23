package org.team4u.id.domain.seq.value;

import org.team4u.base.registrar.PolicyRegistrar;

/**
 * 序号值提供者服务
 *
 * @author jay.wu
 */
public class SequenceProviderFactoryHolder extends PolicyRegistrar<String, SequenceProvider.Factory> {

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
        return create(
                context.getSequenceConfig().getConfigId(),
                context.getSequenceConfig().getSequenceFactoryId(),
                context.getSequenceConfig().getSequenceConfig()
        ).provide(context);
    }

    /**
     * 是否序号值已耗尽
     *
     * @return true：已无可用序号，false：存在可用序号
     */
    public boolean isEmpty(SequenceProvider.Context context) {
        return create(
                context.getSequenceConfig().getConfigId(),
                context.getSequenceConfig().getSequenceFactoryId(),
                context.getSequenceConfig().getSequenceConfig()
        ).isEmpty(context);
    }

    /**
     * 创建序号提供者
     *
     * @param factoryId 工厂标识
     * @param config    工厂配置
     * @return 提供者
     */
    @SuppressWarnings("unchecked")
    public <T extends SequenceProvider> T create(String configId, String factoryId, String config) {
        return (T) availablePolicyOf(factoryId).create(configId, config);
    }
}