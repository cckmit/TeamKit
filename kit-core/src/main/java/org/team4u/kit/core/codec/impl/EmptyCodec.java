package org.team4u.kit.core.codec.impl;

import org.team4u.kit.core.codec.Codec;

/**
 * @author Jay Wu
 */
public class EmptyCodec<V> implements Codec<V, V> {

    @Override
    public V encode(V obj) {
        return obj;
    }

    @Override
    public V decode(V e) {
        return e;
    }
}