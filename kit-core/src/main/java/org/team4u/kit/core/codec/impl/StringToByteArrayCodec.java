package org.team4u.kit.core.codec.impl;

import org.team4u.kit.core.codec.Codec;

/**
 * @author Jay Wu
 */
public class StringToByteArrayCodec implements Codec<String, byte[]> {

    @Override
    public byte[] encode(String obj) {
        if (obj == null) {
            return null;
        }

        return obj.getBytes();
    }

    @Override
    public String decode(byte[] data) {
        if (data == null) {
            return null;
        }

        return new String(data);
    }
}
