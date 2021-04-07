package org.team4u.base.masker.dynamic;

/**
 * 直接修改自身bean属性序列化器
 *
 * @author jay.wu
 */
public class BeanSelfValueSerializer implements DynamicMaskerValueSerializer {

    @Override
    public Object serializeToMaskableObject(Object value) {
        return value;
    }

    @Override
    public String serializeToString(Object value) {
        return value == null ? null : value.toString();
    }
}