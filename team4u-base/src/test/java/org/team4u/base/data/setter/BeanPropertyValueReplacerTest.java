package org.team4u.base.data.setter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.data.setter.BeanPropertyValueReplacer;

public class BeanPropertyValueReplacerTest {

    @Test
    public void replaceListProperty() {
        Dict bean = new Dict().set("a", CollUtil.newArrayList(
                Dict.create().set("b", CollUtil.newArrayList(Dict.create().set("c", 1))),
                Dict.create().set("b", CollUtil.newArrayList(Dict.create().set("c", 2)))
        ));

        BeanPropertyValueReplacer setter = new BeanPropertyValueReplacer();
        setter.replace(bean, "a[].b[].c", it -> it.toString() + it);
        Assert.assertEquals("{a=[{b=[{c=11}]}, {b=[{c=22}]}]}", bean.toString());
    }

    @Test
    public void replaceDirectProperty() {
        Dict bean = new Dict().set("a", Dict.create().set("b", 1));
        BeanPropertyValueReplacer setter = new BeanPropertyValueReplacer();
        setter.replace(bean, "a.b", it -> it.toString() + it);
        Assert.assertEquals("{a={b=11}}", bean.toString());
    }
}