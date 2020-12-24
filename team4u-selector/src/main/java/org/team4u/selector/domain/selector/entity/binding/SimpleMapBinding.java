package org.team4u.selector.domain.selector.entity.binding;

import cn.hutool.core.lang.Dict;

/**
 * 基于map的绑定参数
 *
 * @author jay.wu
 */
public class SimpleMapBinding extends Dict implements SelectorBinding {

    @Override
    public SimpleMapBinding set(String attr, Object value) {
        super.set(attr, value);
        return this;
    }
}