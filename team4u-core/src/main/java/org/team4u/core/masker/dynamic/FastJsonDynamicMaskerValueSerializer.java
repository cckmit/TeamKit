package org.team4u.core.masker.dynamic;

import com.alibaba.fastjson.JSON;
import org.team4u.core.masker.FastJsonMaskerValueSerializer;


/**
 * 基于FastJson的动态掩码器值序列化器
 *
 * @author jay.wu
 */
public class FastJsonDynamicMaskerValueSerializer extends FastJsonMaskerValueSerializer
        implements DynamicMaskerValueSerializer {

    @Override
    public Object serializeToMaskableObject(Object value) {
        return JSON.toJSON(value);
    }
}