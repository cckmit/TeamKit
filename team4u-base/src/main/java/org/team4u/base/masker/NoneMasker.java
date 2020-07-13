package org.team4u.base.masker;

/**
 * 不进行任何处理的掩码器
 * <p>
 * 结果不做处理，直接返回value.toString()
 *
 * @author jay.wu
 */
public class NoneMasker extends AbstractMasker {

    public NoneMasker() {
    }

    public NoneMasker(MaskerValueSerializer serializer) {
        super(serializer);
    }

    @Override
    public String internalMask(Object value) {
        return serializer().serializeToString(value);
    }
}