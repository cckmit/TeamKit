package org.team4u.kit.core.codec;

/**
 * @author Jay Wu
 */
public interface Codec<O, D> {

    /**
     * 将对象编码成byte[]
     *
     * @param obj 对象
     * @return 编码后的值
     */
    D encode(O obj);

    /**
     * 将编码结果解码成对象
     *
     * @param e 编码后的值
     */
    O decode(D e);
}