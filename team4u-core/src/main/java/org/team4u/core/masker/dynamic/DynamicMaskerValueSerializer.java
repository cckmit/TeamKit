package org.team4u.core.masker.dynamic;


import org.team4u.core.masker.MaskerValueSerializer;

/**
 * 动态掩码器值序列化器
 *
 * @author jay.wu
 */
public interface DynamicMaskerValueSerializer extends MaskerValueSerializer {

    /**
     * 将对象序列化为Map或者List结构
     */
    Object serializeToMaskableObject(Object value);
}