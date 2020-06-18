package org.team4u.core.masker;

import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.team4u.core.masker.dynamic.*;

public class DynamicMaskerTest {
    private DynamicMasker masker;

    @Before
    public void setUp() {
        DynamicMaskerConfigRepository repository = new LocalDynamicMaskerConfigRepository();
        DynamicMaskerValueSerializer serializer = new FastJsonDynamicMaskerValueSerializer();

        masker = new DynamicMasker(serializer, repository);
    }

    @Test
    public void maskWithGlobal() {
        String value = masker.mask(new B("fjayblue"));
        Assert.assertEquals("{\"name\":\"*\",\"name2\":\"fjayblue\"}", value);
    }

    @Test
    public void maskWithCustomMask() {
        Maskers.instance().register("TEST", new HideMasker());

        String value = masker.mask(new A("fjayblue"));
        Assert.assertEquals("{\"name\":\"fjayblu*\",\"name2\":\"*\"}", value);
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

    public static class A {

        private String name;
        private String name2;

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
    }

    public static class B extends A {
        public B(String name) {
            super(name);
        }
    }
}