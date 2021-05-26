package org.team4u.selector.domain.selector.value;

import org.team4u.selector.domain.selector.SelectorValueHandler;

/**
 * 固定值处理器
 *
 * @author jay.wu
 */
public class FixedValueHandler implements SelectorValueHandler {
    @Override
    public String id() {
        return "FIXED";
    }

    @Override
    public String handle(Context context) {
        return context.getParams().getStr("value");
    }
}