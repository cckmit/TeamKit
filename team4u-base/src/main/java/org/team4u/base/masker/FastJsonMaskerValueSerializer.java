package org.team4u.base.masker;


import org.team4u.base.util.ConvertUtil;

/**
 * 基于FastJson的掩码器值序列化器
 *
 * @author jay.wu
 */
public class FastJsonMaskerValueSerializer implements MaskerValueSerializer {

    @Override
    public String serializeToString(Object value) {
        return ConvertUtil.toString(value);
    }
}