package org.team4u.id.domain.seq.value.cache;

import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.value.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.SequenceProviderFactoryHolder;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

/**
 * 基于缓存的趋势增长序列服务工厂
 *
 * @author jay.wu
 */
public class CacheStepSequenceProviderFactory extends AbstractSequenceProviderFactory<CacheStepSequenceConfig> {

    @Override
    public String id() {
        return "CS";
    }

    @Override
    protected SequenceProvider createWithConfig(CacheStepSequenceConfig config) {
        StepSequenceProvider delegateProvider = delegateProvider(config);
        return new CacheStepSequenceProvider(config, delegateProvider);
    }

    private StepSequenceProvider delegateProvider(CacheStepSequenceConfig config) {
        return holder().create(
                config.getConfigId(),
                config.getDelegateId(),
                config.getDelegateConfig()
        );
    }

    private SequenceProviderFactoryHolder holder() {
        return BeanProviders.getInstance().getBean(SequenceProviderFactoryHolder.class);
    }
}