package org.team4u.base.serializer;

import cn.hutool.core.lang.Singleton;
import org.team4u.base.serializer.json.JsonCacheableSerializers;
import org.team4u.base.serializer.simple.SimpleValueCacheSerializer;

/**
 * 可缓存的智能序列化器
 *
 * @author jay.wu
 * @see SmartSerializer
 */
public class SmartCacheSerializer extends AbstractSerializers<Serializer> {

    public static SmartCacheSerializer getInstance() {
        return Singleton.get(SmartCacheSerializer.class);
    }

    public SmartCacheSerializer() {
        registerPolicies(JsonCacheableSerializers.getInstance().policies());
        registerPolicy(SimpleValueCacheSerializer.getInstance());
    }
}