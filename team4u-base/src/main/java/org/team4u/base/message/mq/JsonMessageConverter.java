package org.team4u.base.message.mq;

import org.team4u.base.serializer.json.JsonSerializers;

import java.util.UnknownFormatConversionException;

/**
 * 基于Json的消息转换器
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
            return (Target) JsonSerializers.getInstance().serialize(value);
        }

        if (value instanceof String) {
            return JsonSerializers.getInstance().deserialize((String) value, targetType);
        }

        throw new UnknownFormatConversionException(String.format("sourceType=%s,targetType=%s",
                value.getClass().getName(),
                targetType.getName()
        ));
    }
}
