package org.team4u.kit.core.codec.impl;

import org.team4u.kit.core.codec.Codec;

/**
 * @author Jay Wu
 */
public class IntToByteArrayCodec implements Codec<Integer, byte[]> {
    @Override
    public byte[] encode(Integer obj) {
        if (obj == null) {
            return null;
        }

        byte[] data = new byte[4];
        for (int i = 0; i < 4; ++i) {
            int offset = 32 - (i + 1) * 8;
            data[i] = (byte) ((obj >> offset) & 0xff);
        }
        return data;
    }

    @Override
    public Integer decode(byte[] data) {
        if (data == null) {
            return null;
        }

        int num = 0;
        for (int i = 0; i < 4; ++i) {
            num <<= 8;
            num |= (data[i] & 0xff);
        }
        return num;
    }
}
