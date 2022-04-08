package org.team4u.base.serializer.json;

import cn.hutool.core.lang.Singleton;
import org.team4u.base.serializer.AbstractSerializers;

/**
 * JSON组合序列化器
 * <p>
 * 将按优先级挑选第一个可用的json序列化器
 *
 * @author jay.wu
 */
public class JsonSerializers extends AbstractSerializers<JsonSerializer> implements JsonSerializer {

    public static JsonSerializers getInstance() {
        return Singleton.get(JsonSerializers.class);
    }
}