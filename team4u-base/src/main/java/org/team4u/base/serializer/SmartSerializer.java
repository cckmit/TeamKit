package org.team4u.base.serializer;

import cn.hutool.core.lang.Singleton;
import org.team4u.base.serializer.json.JsonSerializers;
import org.team4u.base.serializer.simple.SimpleValueSerializer;

/**
 * 智能序列化器
 * <p>
 * - 可自动根据值类型进行数据转换
 * <p>
 * - 目前支持JSON和简单类型转换，如“y”可转换为true，{"x":"x"}可转换为对象
 *
 * @author jay.wu
 */
public class SmartSerializer extends AbstractSerializers<Serializer> {

    public static SmartSerializer getInstance() {
        return Singleton.get(SmartSerializer.class);
    }

    public SmartSerializer() {
        registerPolicies(JsonSerializers.getInstance().policies());
        registerPolicy(SimpleValueSerializer.getInstance());
    }
}