package org.team4u.base.lang;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author jay.wu
 */
public class EasyMap extends Dict {

    public EasyMap() {
    }

    public EasyMap(boolean caseInsensitive) {
        super(caseInsensitive);
    }

    public EasyMap(int initialCapacity) {
        super(initialCapacity);
    }

    public EasyMap(int initialCapacity, boolean caseInsensitive) {
        super(initialCapacity, caseInsensitive);
    }

    public EasyMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public EasyMap(int initialCapacity, float loadFactor, boolean caseInsensitive) {
        super(initialCapacity, loadFactor, caseInsensitive);
    }

    public EasyMap(Map<String, Object> m) {
        super(m);
    }

    @SuppressWarnings("unchecked")
    public <V> V getProperty(String expression, V defaultValue) {
        return (V) ObjectUtil.defaultIfNull(getProperty(expression, defaultValue.getClass()), defaultValue);
    }

    public <V> V getProperty(String expression, Class<V> clazz) {
        if (this.isEmpty()) {
            return null;
        }

        return Convert.convert((Type) clazz, BeanUtil.getProperty(this, expression));
    }

    public Object getProperty(String expression) {
        return BeanUtil.getProperty(this, expression);
    }

    public void setProperty(String expression, Object value) {
        BeanUtil.setProperty(this, expression, value);
    }

    @Override
    public EasyMap set(String attr, Object value) {
        super.set(attr, value);
        return this;
    }

    @Override
    public EasyMap setIgnoreNull(String attr, Object value) {
        super.setIgnoreNull(attr, value);
        return this;
    }

    @Override
    public <T> EasyMap parseBean(T bean) {
        super.parseBean(bean);
        return this;
    }
}