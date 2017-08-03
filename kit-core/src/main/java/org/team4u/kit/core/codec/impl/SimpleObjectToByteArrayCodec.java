package org.team4u.kit.core.codec.impl;

import com.xiaoleilu.hutool.util.ObjectUtil;
import org.team4u.kit.core.codec.Codec;

/**
 * @author Jay Wu
 */
public class SimpleObjectToByteArrayCodec<V> implements Codec<V, byte[]> {

    @Override
    public byte[] encode(V obj) {
        return ObjectUtil.serialize(obj);
    }

    @Override
    public V decode(byte[] bytes) {
        return ObjectUtil.unserialize(bytes);
    }
}