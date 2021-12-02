package org.team4u.id.application;

import cn.hutool.core.lang.generator.Generator;
import lombok.Getter;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.SequenceConfigRepository;
import org.team4u.id.domain.seq.group.SequenceGroupKeyFactoryHolder;
import org.team4u.id.domain.seq.group.SequenceGroupKeyProvider;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.SequenceProviderFactoryHolder;

import java.util.Collections;
import java.util.Map;

/**
 * 序号生成器
 *
 * @author jay.wu
 */
public class SequenceGenerator implements Generator<Number> {

    public static final String GLOBAL_CONFIG_ID = "seq.global";

    @Getter
    private final SequenceConfigRepository sequenceConfigRepository;
    @Getter
    private final SequenceProviderFactoryHolder valueFactoryHolder;
    @Getter
    private final SequenceGroupKeyFactoryHolder groupFactoryHolder;

    public SequenceGenerator(SequenceConfigRepository sequenceConfigRepository) {
        this.sequenceConfigRepository = sequenceConfigRepository;

        this.valueFactoryHolder = new SequenceProviderFactoryHolder();
        this.groupFactoryHolder = new SequenceGroupKeyFactoryHolder();

        BeanProviders.getInstance().registerBean(valueFactoryHolder);
        BeanProviders.getInstance().registerBean(groupFactoryHolder);
    }

    /**
     * 生成下一个标识
     * <p>
     * 使用默认配置 GLOBAL_CONFIG_ID
     */
    @Override
    public Number next() {
        return next(GLOBAL_CONFIG_ID);
    }

    /**
     * 生成下一个标识
     *
     * @param configId 配置标识
     */
    public Number next(String configId) {
        return next(configId, Collections.emptyMap());
    }

    /**
     * 生成下一个标识
     *
     * @param configId 配置标识
     * @param context  上下文
     * @return 序号值
     */
    public Number next(String configId, Map<String, Object> context) {
        SequenceConfig config = sequenceConfigRepository.configOfId(configId);
        String groupKey = groupFactoryHolder.provide(
                new SequenceGroupKeyProvider.Context(config, context)
        );
        return valueFactoryHolder.provide(
                new SequenceProvider.Context(config, groupKey, context)
        );
    }
}