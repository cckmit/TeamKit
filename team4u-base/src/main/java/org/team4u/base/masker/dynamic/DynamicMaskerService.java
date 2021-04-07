package org.team4u.base.masker.dynamic;

/**
 * 动态掩码器服务
 *
 * @author jay.wu
 */
public class DynamicMaskerService {

    private static final DynamicMaskerService INSTANCE = new DynamicMaskerService(new LocalDynamicMaskerConfigRepository());

    private DynamicMasker beanMasker;
    private DynamicMasker defaultMasker;

    public DynamicMaskerService(DynamicMaskerConfigRepository dynamicMaskerConfigRepository) {
        beanMasker = new DynamicMasker(
                new BeanSelfValueSerializer(),
                dynamicMaskerConfigRepository
        );

        defaultMasker = new DynamicMasker(
                new FastJsonDynamicMaskerValueSerializer(),
                dynamicMaskerConfigRepository
        );
    }

    public static DynamicMaskerService instance() {
        return INSTANCE;
    }

    public DynamicMasker getBeanMasker() {
        return beanMasker;
    }

    public DynamicMasker getDefaultMasker() {
        return defaultMasker;
    }

    /**
     * 掩码
     *
     * @param value 原始值
     * @return 掩码值
     */
    public String mask(Object value) {
        return defaultMasker.mask(value);
    }

    /**
     * 掩码并替换字段
     * <p>
     * 注意，该方法将直接替换原始对象的属性值
     *
     * @param value 原始值
     * @return 掩码后的对象
     */
    public Object maskAndReplace(Object value) {
        beanMasker.mask(value);
        return value;
    }

    public void setDynamicMaskerConfigRepository(DynamicMaskerConfigRepository repository) {
        beanMasker = new DynamicMasker(
                new BeanSelfValueSerializer(),
                repository
        );

        defaultMasker = new DynamicMasker(
                new FastJsonDynamicMaskerValueSerializer(),
                repository
        );
    }
}