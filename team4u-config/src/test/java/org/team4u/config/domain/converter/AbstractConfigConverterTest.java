package org.team4u.config.domain.converter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.TypeReference;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigConverter;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.domain.SimpleConfigs;

import java.util.List;
import java.util.Map;

import static org.team4u.config.TestUtil.c;

public abstract class AbstractConfigConverterTest {

    protected final MockSimpleConfigRepository repository = new MockSimpleConfigRepository();

    protected final SimpleConfigConverter converter = newSimpleConfigConverter();

    protected abstract SimpleConfigConverter newSimpleConfigConverter();

    @Test
    public void type() {
        TestConfig config = converter.to(
                TestConfig.class,
                TestUtil.TEST_ID
        );

        Assert.assertEquals(1, config.getA());
        Assert.assertArrayEquals(new String[]{"1", "2"}, config.getB());
        Assert.assertEquals(CollUtil.newArrayList(1, 2), config.getC());
        Assert.assertEquals("1", config.getD().get("test"));
        Assert.assertEquals(new Demo("1", "2"), config.getE().get(0));

        additionalTypeCheck(config);
    }

    protected void additionalTypeCheck(TestConfig config) {

    }

    @Test
    public void value() {
        Integer a = converter.to(
                Integer.class,
                TestUtil.TEST_ID,
                "a"
        );

        Assert.assertEquals(1, a.intValue());

        String[] b = converter.to(
                String[].class,
                TestUtil.TEST_ID,
                "b"
        );

        Assert.assertArrayEquals(new String[]{"1", "2"}, b);

        List<Demo> e = converter.to(
                new TypeReference<List<Demo>>() {

                }.getType(),
                TestUtil.TEST_ID,
                "e"
        );
        Assert.assertEquals(new Demo("1", "2"), e.get(0));
    }

    @Data
    public static class TestConfig {
        private int a;
        private String[] b;
        private List<Integer> c;
        private Map<String, String> d;
        private List<Demo> e;
        private boolean f;
    }

    @Data
    public static class Demo {
        private String a;
        private String b;

        public Demo(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    public static class MockSimpleConfigRepository implements SimpleConfigRepository {

        private final List<SimpleConfig> list = CollUtil.newArrayList(
                c("a", "1"),
                c("b", "1,2"),
                c("c", "1,2"),
                c("d", "{\"test\":1}"),
                c("e", "[\n" +
                        "  {\n" +
                        "    \"a\": \"1\",\n" +
                        "    \"b\": \"2\",\n" +
                        "    \"c\": \"3\"\n" +
                        "  }\n" +
                        "]"),
                c("f", "true"),
                c(TestUtil.TEST_ID2, "a", "2")
        );

        @Override
        public SimpleConfigs allConfigs() {
            return new SimpleConfigs(list);
        }
    }
}