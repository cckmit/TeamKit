package org.team4u.kit.core.codec;

import org.team4u.kit.core.codec.impl.*;

/**
 * @author Jay Wu
 */
public class CodecRegistry {

    public static final StringToByteArrayCodec STRING_TO_BYTE_ARRAY_CODEC = new StringToByteArrayCodec();
    public static final LongToByteArrayCodec LONG_TO_BYTE_ARRAY_CODEC = new LongToByteArrayCodec();
    public static final IntToByteArrayCodec INT_TO_BYTE_ARRAY_CODEC = new IntToByteArrayCodec();

    public static final EmptyCodec EMPTY_CODEC = new EmptyCodec();

    public static final UrlToMapCodec URL_TO_MAP_CODEC = new UrlToMapCodec();
    public static final UrlToMapListCodec URL_TO_MAP_LIST_CODEC = new UrlToMapListCodec();

}