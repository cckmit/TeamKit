package org.team4u.selector.domain.selector;

import cn.hutool.core.convert.Convert;
import lombok.Data;

import java.lang.reflect.Type;

@Data
public class SelectorResult {

    public static final SelectorResult NOT_MATCH = new SelectorResult(null);

    public static final SelectorResult MATCH = new SelectorResult(null, true);

    /**
     * 最终选择结果
     */
    private final Object value;

    private final boolean match;

    public SelectorResult(Object value, boolean match) {
        this.value = value;
        this.match = match;
    }

    public SelectorResult(Object value) {
        this(value, value != null);
    }

    public static SelectorResult createWithMatch(boolean match) {
        return new SelectorResult(null, match);
    }

    public static SelectorResult createWithValue(Object value) {
        return new SelectorResult(value);
    }

    public <T> T convert(Type type) {
        return Convert.convert(type, value);
    }

    public String toStr() {
        return convert(String.class);
    }
}