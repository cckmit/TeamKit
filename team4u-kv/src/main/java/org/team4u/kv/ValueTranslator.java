package org.team4u.kv;

/**
 * 值对象转换器
 *
 * @author jay.wu
 */
public interface ValueTranslator {

    /**
     * 将值对象转换为字符串
     */
    String translateToString(Object v);

    /**
     * 将字符串转换为指定类型的值对象
     */
    <V> V translateToValue(Class<V> valueClass, String value);
}