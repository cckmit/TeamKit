package org.team4u.ddd.infrastructure.message;

import com.alibaba.fastjson.JSON;
import org.team4u.ddd.message.MessageConverter;

import java.util.UnknownFormatConversionException;

/**
 * 基于FastJson的消息转换器
 *
 * @author jay.wu
 */
public class JsonMessageConverter implements MessageConverter {

    @SuppressWarnings("unchecked")
    @Override
    public <Target> Target convert(Object value, Class<Target> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType == String.class) {
            return (Target) JSON.toJSONString(value);
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value, targetType);
        }

        throw new UnknownFormatConversionException(String.format("sourceType=%s,targetType=%s",
                value.getClass().getName(),
                targetType.getName()
        ));
    }
}
