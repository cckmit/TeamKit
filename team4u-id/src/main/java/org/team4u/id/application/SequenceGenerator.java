package org.team4u.id.application;

import cn.hutool.core.lang.generator.Generator;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.SequenceConfigRepository;
import org.team4u.id.domain.seq.SequenceProvider;
import org.team4u.id.domain.seq.SequenceProviderFactoryHolder;
import org.team4u.id.domain.seq.group.SequenceGroupKeyProvider;
import org.team4u.id.domain.seq.group.SequenceGroupKeyProviderFactoryHolder;

import java.util.Collections;
import java.util.Map;

/**
 * 序号生成器
 *
 * @author jay.wu
 */
public class SequenceGenerator implements Generator<Number> {

    public static final String GLOBAL_CONFIG_ID = "seq.global";

    private final SequenceConfigRepository sequenceConfigRepository;

    private final SequenceProviderFactoryHolder noProviderFactoryHolder;
    private final SequenceGroupKeyProviderFactoryHolder groupKeyProviderFactoryHolder;

    public SequenceGenerator(SequenceConfigRepository sequenceConfigRepository) {
        this.sequenceConfigRepository = sequenceConfigRepository;

        this.noProviderFactoryHolder = new SequenceProviderFactoryHolder();
        this.groupKeyProviderFactoryHolder = new SequenceGroupKeyProviderFactoryHolder();
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
        String groupKey = groupKeyProviderFactoryHolder.provide(
                new SequenceGroupKeyProvider.Context(config, context)
        );
        return noProviderFactoryHolder.provide(
                new SequenceProvider.Context(config, groupKey, context)
        );
    }

    public SequenceProviderFactoryHolder noProviderFactoryHolder() {
        return noProviderFactoryHolder;
    }

    public SequenceGroupKeyProviderFactoryHolder groupKeyProviderFactoryHolder() {
        return groupKeyProviderFactoryHolder;
    }

    /**
     * 生成下一个标识
     * <p>
     * 使用默认配置 GLOBAL_CONFIG_ID
     */
    @Override
    public Number next() {
        return next(GLOBAL_CONFIG_ID, Collections.emptyMap());
    }
}