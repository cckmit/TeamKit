package org.team4u.core.masker;

/**
 * 掩码器值序列化器
 *
 * @author jay.wu
 */
public interface MaskerValueSerializer {

    /**
     * 将对象序列化为字符串
     */
    String serializeToString(Object value);
}
