package org.team4u.kv.infrastruture.translator;

import org.team4u.core.util.ConvertUtil;
import org.team4u.kv.ValueTranslator;

/**
 * 基于fastjson的转换器
 *
 * @author jay.wu
 */
public class JsonValueTranslator implements ValueTranslator {

    @Override
    public String translateToString(Object v) {
        return ConvertUtil.toString(v);
    }

    @Override
    public <V> V translateToValue(Class<V> valueClass, String value) {
        return ConvertUtil.convert(value, valueClass);
    }
}