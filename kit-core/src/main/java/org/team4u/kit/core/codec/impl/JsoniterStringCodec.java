package org.team4u.kit.core.codec.impl;

import com.alibaba.fastjson.JSON;
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

        return JSON.toJSONString(obj);
    }

    @Override
    public V decode(String e) {
        if (StrUtil.isBlank(e)) {
            return null;
        }

        return JSON.parseObject(e, targetClass);
    }
}