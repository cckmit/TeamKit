package org.team4u.base.serializer;

import cn.hutool.core.util.ClassUtil;
import org.team4u.base.registrar.PolicyRegistrar;

import java.lang.reflect.Type;

/**
 * 抽象组合序列化器
 *
 * @author jay.wu
 */
public abstract class AbstractSerializers<S extends Serializer> extends PolicyRegistrar<Object, S> implements Serializer {

    @Override
    public String serialize(Object value) {
        return availablePolicyOf(value).serialize(value);
    }

    @Override
    public <T> T deserialize(String serialization, Class<T> type) {
        return availablePolicyOf(serialization).deserialize(serialization, type);
    }

    @Override
    public <T> T deserialize(String serialization, Type type) {
        return availablePolicyOf(serialization).deserialize(serialization, type);
    }

    @Override
    public boolean supports(Object context) {
        return availablePolicyOf(context) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<S> getPolicyType() {
        return (Class<S>) ClassUtil.getTypeArgument(this.getClass());
    }

    @Override
    public Class<Object> getContextType() {
        return Object.class;
    }
}