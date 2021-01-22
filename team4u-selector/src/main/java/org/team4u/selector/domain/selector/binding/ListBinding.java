package org.team4u.selector.domain.selector.binding;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 基于List的绑定参数
 *
 * @author jay.wu
 */
public class ListBinding extends ArrayList<Object> implements SelectorBinding {

    public ListBinding(int initialCapacity) {
        super(initialCapacity);
    }

    public ListBinding() {
    }

    public ListBinding(Collection<?> c) {
        super(c);
    }

    public ListBinding addValue(Object o) {
        add(o);
        return this;
    }
}