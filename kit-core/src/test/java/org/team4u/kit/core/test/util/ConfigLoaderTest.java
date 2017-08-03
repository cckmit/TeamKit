package org.team4u.kit.core.test.util;


import com.xiaoleilu.hutool.util.CollectionUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.config.ConfigLoader;
import org.team4u.kit.core.config.Configurable;
import org.team4u.kit.core.config.Configurer;
import org.team4u.kit.core.util.MapUtil;

import java.util.List;

public class ConfigLoaderTest {

    @Test
    public void defaultLoader() throws Exception {
        ConfigA a = Configurer.getInstance().loadWithFilePath(ConfigA.class, "config.js");
        check(a);

        a = Configurer.getInstance().loadWithFilePath(ConfigA.class, "config.properties");
        check(a);

        a = Configurer.getInstance().loadWithContent(
                ConfigA.class,
                Configurer.LoaderType.MAP.getKey(),
                MapUtil.toMap(a));
        check(a);
    }

    private void check(ConfigA a) {
        Assert.assertEquals("t1", a.a.name);
        Assert.assertEquals(Integer.valueOf(1), a.a.value);
        Assert.assertEquals(1, a.a.numbers.get(0).intValue());

        Assert.assertEquals(1, a.b.intValue());
        Assert.assertEquals(CollectionUtil.newArrayList(1, 2, 3), a.c);
    }

    @Test
    public void custom() {
        Configurer.getInstance().registerLoader(new ConfigLoader<String>() {

            @Override
            @SuppressWarnings("unchecked")
            public <T> T load(Class<T> configClass, String content) {
                ConfigA a = new ConfigA();
                a.b = 2;
                return (T) a;
            }

            @Override
            public String getKey() {
                return ".test";
            }
        });

        Assert.assertEquals(2,
                Configurer.getInstance().loadWithContent(ConfigA.class, ".test", null).b.intValue());
    }

    public static class ConfigA implements Configurable {

        public A a;
        public Integer b;
        public List<Integer> c;

        @Override
        public String getKey() {
            return this.getClass().getName();
        }

        public static class A {
            public String name;
            public Integer value;
            public List<Integer> numbers;
        }
    }
}