package org.team4u.base.masker.dynamic;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.data.setter.BeanPropertyValueReplacer;
import org.team4u.base.masker.AbstractMasker;
import org.team4u.base.masker.Masker;

import java.util.List;
import java.util.Map;

/**
 * 动态掩码器
 *
 * @author jay.wu
 */
public class DynamicMasker extends AbstractMasker {

    private static final ThreadLocal<DynamicMaskerContext> MASKER_CONTEXTS = new ThreadLocal<>();
    private final BeanPropertyValueReplacer beanPropertyValueReplacer;
    private final DynamicMaskerConfigRepository repository;

    public DynamicMasker(DynamicMaskerValueSerializer serializer,
                         DynamicMaskerConfigRepository repository) {
        super(serializer);
        this.repository = repository;

        this.beanPropertyValueReplacer = new BeanPropertyValueReplacer();
    }

    @Override
    public String internalMask(Object value) {
        DynamicMaskerConfig config = configOf(value);

        if (config == null) {
            return serializer().serializeToString(value);
        }

        try {
            return maskWithConfig(config, value);
        } finally {
            // 清理掩码上下文
            removeContext();
        }
    }

    /**
     * 掩码时的上下文
     */
    public DynamicMaskerContext context() {
        DynamicMaskerContext context = MASKER_CONTEXTS.get();

        if (context == null) {
            context = new DynamicMaskerContext();
            MASKER_CONTEXTS.set(context);
        }

        return context;
    }

    /**
     * 清理掩码上下文
     */
    public void removeContext() {
        MASKER_CONTEXTS.remove();
    }

    /**
     * 根据配置进行掩码
     */
    private String maskWithConfig(DynamicMaskerConfig config, Object value) {
        if (config.isGlobal()) {
            return new GlobalDynamicMasker(config).mask(value);
        }

        Object maskableObject = ((DynamicMaskerValueSerializer) serializer()).serializeToMaskableObject(value);

        for (Map.Entry<Masker, List<String>> entry : config.getMaskerExpressions().entrySet()) {
            Masker masker = entry.getKey();
            if(masker == null){
                continue;
            }

            List<String> expressions = entry.getValue();
            for (String expression : expressions) {
                // 表达式为指向自身，直接掩码处理
                if (matchValuePath(expression)) {
                    maskableObject = masker.mask(maskableObject);
                } else {
                    // 表达式指向内部属性
                    beanPropertyValueReplacer.replace(
                            maskableObject,
                            standardizingBeanExpression(expression),
                            masker::mask
                    );
                }
            }
        }

        return serializer().serializeToString(maskableObject);
    }


    /**
     * 根据值获取掩码配置
     */
    private DynamicMaskerConfig configOf(Object value) {
        List<DynamicMaskerConfig> configs = repository.allConfigs();

        return configOf(
                configs,
                new String[]{
                        configIdOfValueClass(value),
                        configIdOfContext(),
                        DynamicMaskerConfig.GLOBAL_CONFIG_ID
                }
        );
    }

    private boolean matchValuePath(String expression) {
        return StrUtil.equals(expression, valuePathOfContext());
    }

    private String standardizingBeanExpression(String expression) {
        String valuePathOfContext = valuePathOfContext();
        if (valuePathOfContext == null) {
            return expression;
        }

        if (expression.startsWith(valuePathOfContext)) {
            return expression.replaceFirst(valuePathOfContext + "\\.", "");
        }

        return expression;
    }

    private String valuePathOfContext() {
        DynamicMaskerContext context = MASKER_CONTEXTS.get();

        if (context == null) {
            return null;
        }

        return context.getValuePath();
    }

    /**
     * 从值类型获取配置标识
     */
    private String configIdOfValueClass(Object value) {
        return value.getClass().getName();
    }

    /**
     * 上下文内的配置标识
     */
    private String configIdOfContext() {
        DynamicMaskerContext context = MASKER_CONTEXTS.get();

        if (context == null) {
            return null;
        }

        return context.getConfigId();
    }

    /**
     * 根据给定的配置标识，按优先级依次查找可用的配置对象
     *
     * @param configs   配置对象集合
     * @param configIds 配置标识集合，排在在前面的标识优先级高
     * @return 返回第一个可用的配置对象
     */
    private DynamicMaskerConfig configOf(List<DynamicMaskerConfig> configs, String[] configIds) {
        for (String configId : configIds) {
            DynamicMaskerConfig config = configOf(configs, configId);
            if (config != null) {
                return config;
            }
        }

        return null;
    }

    private DynamicMaskerConfig configOf(List<DynamicMaskerConfig> configs, String configId) {
        if (configId == null) {
            return null;
        }

        return configs.stream()
                .filter(it -> StrUtil.equals(it.getConfigId(), configId))
                .findFirst()
                .orElse(null);
    }
}