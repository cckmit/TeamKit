package org.team4u.core.masker;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 * 抽象掩码器
 *
 * @author jay.wu
 */
public abstract class AbstractMasker implements Masker {

    private Log log = LogFactory.get();

    private MaskerValueSerializer serializer;

    protected AbstractMasker() {
        this(new SimpleMaskerValueSerializer());
    }

    protected AbstractMasker(MaskerValueSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public String mask(Object value) {
        if (value == null) {
            return null;
        }

        try {
            return internalMask(value);
        } catch (Exception e) {
            log.warn("internalMask error", e);
            try {
                return serializer.serializeToString(value);
            } catch (Exception e2) {
                log.warn("Masker serializeToString error", e2);
                return value.toString();
            }
        }
    }

    protected abstract String internalMask(Object value);

    public MaskerValueSerializer serializer() {
        return serializer;
    }
}