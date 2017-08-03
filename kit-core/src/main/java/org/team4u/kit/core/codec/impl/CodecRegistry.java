package org.team4u.kit.core.codec.impl;

/**
 * @author Jay Wu
 */
public class CodecRegistry {

    public static final StringToByteArrayCodec STRING_TO_BYTE_ARRAY_CODEC = new StringToByteArrayCodec();

    public static final LongToByteArrayCodec LONG_TO_BYTE_ARRAY_CODEC = new LongToByteArrayCodec();

    public static final IntToByteArrayCodec INT_TO_BYTE_ARRAY_CODEC = new IntToByteArrayCodec();

    public static final EmptyCodec EMPTY_CODEC = new EmptyCodec();
}