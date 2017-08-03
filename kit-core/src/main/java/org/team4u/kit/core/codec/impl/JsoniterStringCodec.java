package org.team4u.kit.core.codec.impl;

import com.jsoniter.JsonIterator;
import com.jsoniter.output.JsonStream;
import com.xiaoleilu.hutool.util.ClassUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.team4u.kit.core.codec.Codec;

/**
 * @author Jay Wu
 */
public abstract class JsoniterStringCodec<V> implements Codec<V, String> {

    @SuppressWarnings("unchecked")
    private Class<V> targetClass = (Class<V>) ClassUtil.getTypeArgument(this.getClass(), 0);

    @Override
    public String encode(V obj) {
        if (obj == null) {
            return null;
        }

        return JsonStream.serialize(obj);
    }

    @Override
    public V decode(String e) {
        if (StrUtil.isBlank(e)) {
            return null;
        }

        return JsonIterator.deserialize(e, targetClass);
    }
}