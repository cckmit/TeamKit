package org.team4u.kit.core.codec.impl;

import org.team4u.kit.core.codec.Codec;

/**
 * @author Jay Wu
 */
public class LongToByteArrayCodec implements Codec<Long, byte[]> {

    @Override
    public byte[] encode(Long obj) {
        if (obj == null) {
            return null;
        }

        byte[] data = new byte[8];
        for (int i = 0; i < 8; ++i) {
            int offset = 64 - (i + 1) * 8;
            data[i] = (byte) ((obj >> offset) & 0xff);
        }
        return data;
    }

    @Override
    public Long decode(byte[] data) {
        if (data == null) {
            return null;
        }

        long num = 0;
        for (int i = 0; i < 8; ++i) {
            num <<= 8;
            num |= (data[i] & 0xff);
        }
        return num;
    }
}
