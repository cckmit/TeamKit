package org.team4u.base.serializer;


import cn.hutool.core.lang.TypeReference;

import java.lang.reflect.Type;

/**
 * 序列化器
 *
 * @author jay.wu
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param value 来源对象
     * @return 序列化后的值
     */
    String serialize(Object value);

    /**
     * 反序列化
     *
     * @param serialization 序列化后的值
     * @param type          类型
     * @param <T>           类型
     * @return 来源对象
     */
    <T> T deserialize(String serialization, Class<T> type);

    /**
     * @param serialization 序列化后的值
     * @param type          从TypeReference获取
     * @param <T>           类型
     * @return 来源对象
     * @see TypeReference
     */
    <T> T deserialize(String serialization, Type type);
}