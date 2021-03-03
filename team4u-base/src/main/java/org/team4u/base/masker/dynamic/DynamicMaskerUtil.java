package org.team4u.base.masker.dynamic;

/**
 * 动态掩码器
 *
 * @author jay.wu
 */
public class DynamicMaskerUtil {

    private static DynamicMasker MASKER = new DynamicMasker(
            new FastJsonDynamicMaskerValueSerializer(),
            new LocalDynamicMaskerConfigRepository()
    );

    /**
     * 仅供测试使用
     *
     * @param masker 掩码器
     */
    public static void setMasker(DynamicMasker masker) {
        MASKER = masker;
    }

    /**
     * 进行掩码
     *
     * @param value 原始值
     * @return 掩码值
     */
    public static String mask(Object value) {
        return MASKER.mask(value);
    }

    public static DynamicMasker masker() {
        return MASKER;
    }
}