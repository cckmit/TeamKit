package org.team4u.ddd.infrastructure.message;

import com.alibaba.fastjson.JSON;

import java.util.UnknownFormatConversionException;

public class JsonMessageConverter implements org.team4u.ddd.message.MessageConverter {

    @SuppressWarnings("unchecked")
    @Override
    public <Target> Target convert(Object value, Class<Target> targetType) {
        if (targetType == String.class) {
            return (Target) JSON.toJSONString(value);
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value, targetType);
        }

        throw new UnknownFormatConversionException(String.format("sourceType=%s,targetType=%s",
                value == null ? null : value.getClass().getName(),
                targetType.getName()
        ));
    }
}
