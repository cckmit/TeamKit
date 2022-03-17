package org.team4u.base.masker;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.base.masker.dynamic.DynamicMasker;
import org.team4u.base.masker.dynamic.DynamicMaskerService;

import java.util.List;
import java.util.Map;

public class DynamicMaskerTest {

    private final DynamicMasker masker = DynamicMaskerService.instance().getDefaultMasker();

    @Test
    public void maskWithGlobal() {
        String value = masker.mask(
                new B("fjayblue")
                        .setMap(JSON.parseObject(FileUtil.readUtf8String("complex.json")))
                        .setList(CollUtil.newArrayList(JSON.parseObject(FileUtil.readUtf8String("complex.json"))))
        );
        Assert.assertEquals("{\"list\":[{\"a\":[{\"b\":[{\"c\":{\"name\":\"*\"}}]}],\"b\":{\"c\":{\"d\":{\"name\":\"*\"}}}}],\"map\":{\"a\":[{\"b\":[{\"c\":{\"name\":\"*\"}}]}],\"b\":{\"c\":{\"d\":{\"name\":\"*\"}}}},\"name\":\"*\",\"name2\":\"fjayblue\"}", value);
    }

    @Test
    public void maskWithCustomMask() {
        Maskers.instance().register("TEST", new HideMasker());

        String value = masker.mask(new A("fjayblue"));
        Assert.assertEquals("{\"name\":\"f******e\",\"name2\":\"*\"}", value);
    }

    @Test
    public void maskWithTag1() {
        masker.context().setConfigId("x.y");
        masker.context().setValuePath("a");
        String value = masker.mask(Dict.create().set("name", "fjay"));
        Assert.assertEquals("{\"name\":\"*\"}", value);
    }

    @Test
    public void maskWithTag2() {
        masker.context().setConfigId("x.y");
        masker.context().setValuePath("z");
        String value = masker.mask(Dict.create());
        Assert.assertEquals("*", value);
    }

    @Test
    public void maskBeanSelf() {
        Maskers.instance().register("TEST", new HideMasker());

        DynamicMasker masker = DynamicMaskerService.instance().getBeanMasker();

        A a = new A("fjayblue");
        String value = masker.mask(a);

        Assert.assertEquals(a.toString(), value);
        Assert.assertEquals("*", a.getName2());
    }

    public static class A {

        private final String name;
        private final String name2;

        private Map<String, Object> map;
        private List<Map<String, Object>> list;

        public A(String name) {
            this.name = name;
            this.name2 = name;
        }

        public String getName() {
            return name;
        }

        public String getName2() {
            return name2;
        }

        public Map<String, Object> getMap() {
            return map;
        }

        public A setMap(Map<String, Object> map) {
            this.map = map;
            return this;
        }

        public List<Map<String, Object>> getList() {
            return list;
        }

        public A setList(List<Map<String, Object>> list) {
            this.list = list;
            return this;
        }
    }

    public static class B extends A {
        public B(String name) {
            super(name);
        }
    }
}