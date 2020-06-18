package org.team4u.core.masker;

/**
 * 简单的掩码器值序列化器
 * <p>
 * 直接使用对象的toString方法
 *
 * @author jay.wu
 */
public class SimpleMaskerValueSerializer implements MaskerValueSerializer {

    @Override
    public String serializeToString(Object value) {
        if (value == null) {
            return null;
        }

        return value.toString();
    }
}
