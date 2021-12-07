package org.team4u.id.application;

import cn.hutool.core.lang.generator.Generator;
import lombok.Getter;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.SequenceConfigLoadException;
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
     *
     * @return 序号值，若无可用序号时返回null
     * @throws SequenceConfigLoadException 当配置加载出错时抛出此异常
     */
    @Override
    public Number next() throws SequenceConfigLoadException {
        return next(GLOBAL_CONFIG_ID);
    }

    /**
     * 生成下一个标识
     *
     * @param configId 配置标识
     * @return 序号值，若无可用序号时返回null
     * @throws SequenceConfigLoadException 当配置加载出错时抛出此异常
     */
    public Number next(String configId) throws SequenceConfigLoadException {
        return next(configId, false);
    }

    /**
     * 生成下一个标识
     *
     * @param configId             配置标识
     * @param nullIfConfigNotExist 当配置不存在时是否返回null，true返回null，false则抛出SequenceConfigLoadException
     * @return 序号值，若无可用序号时返回null
     * @throws SequenceConfigLoadException 当配置加载出错时抛出此异常
     */
    public Number next(String configId, boolean nullIfConfigNotExist) throws SequenceConfigLoadException {
        return next(configId, nullIfConfigNotExist, Collections.emptyMap());
    }

    /**
     * 生成下一个标识
     *
     * @param configId             配置标识
     * @param context              上下文
     * @param nullIfConfigNotExist 当配置不存在时是否返回null，true返回null，false则抛出SequenceConfigLoadException
     * @return 序号值，若无可用序号时返回null
     * @throws SequenceConfigLoadException 当配置加载出错时抛出此异常
     */
    public Number next(String configId,
                       boolean nullIfConfigNotExist,
                       Map<String, Object> context) throws SequenceConfigLoadException {
        SequenceConfig config = configOf(configId, nullIfConfigNotExist);
        String groupKey = groupFactoryHolder.provide(
                new SequenceGroupKeyProvider.Context(config, context)
        );
        return valueFactoryHolder.provide(
                new SequenceProvider.Context(config, groupKey, context)
        );
    }

    private SequenceConfig configOf(String configId, boolean nullIfConfigNotExist) throws SequenceConfigLoadException {
        SequenceConfig config = sequenceConfigRepository.configOfId(configId);
        if (config != null) {
            return config;
        }

        if (nullIfConfigNotExist) {
            return null;
        }

        throw new SequenceConfigLoadException("Unable to find SequenceConfig|configId=" + configId);
    }
}