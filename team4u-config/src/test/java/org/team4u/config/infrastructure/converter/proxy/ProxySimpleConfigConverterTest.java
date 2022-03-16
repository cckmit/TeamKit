package org.team4u.config.infrastructure.converter.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.thread.ThreadUtil;
import lombok.Data;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigRepository;
import org.team4u.config.infrastructure.persistence.CacheableSimpleConfigRepository;
import org.team4u.config.infrastructure.persistence.MapSimpleConfigRepository;
import org.team4u.test.Benchmark;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.team4u.config.TestUtil.c;

public class ProxySimpleConfigConverterTest {

    @Test
    public void type() {
        MockSimpleConfigRepository repository = new MockSimpleConfigRepository();
        ProxySimpleConfigConverter converter = new ProxySimpleConfigConverter(repository);
        TestConfig config = converter.to(
                TestConfig.class,
                TestUtil.TEST_ID
        );

        Assert.assertEquals(1, config.getA());
        Assert.assertArrayEquals(new String[]{"1", "2"}, config.getB());
        Assert.assertEquals(CollUtil.newArrayList(1, 2), config.getC());
        Assert.assertEquals("1", config.getD().get("test"));

        // 配置项变动
        repository.allConfigs().get(0).setConfigValue("2");
        ThreadUtil.sleep(1000);
        Assert.assertEquals(2, config.getA());
        Assert.assertTrue(config.isF());
    }

    @Test
    @Ignore
    public void benchmark() {
        SimpleConfigRepository repository1 = new MapSimpleConfigRepository(
                Dict.create().set(TestUtil.TEST_ID + ".a", "1")
        );
        CacheableSimpleConfigRepository repository2 = new CacheableSimpleConfigRepository(
                new CacheableSimpleConfigRepository.Config().setMaxEffectiveSec(5),
                repository1
        );
        ProxySimpleConfigConverter converter = new ProxySimpleConfigConverter(repository2);
        TestConfig config = converter.to(
                TestConfig.class,
                TestUtil.TEST_ID
        );

        Benchmark benchmark = new Benchmark();
        benchmark.setPrintError(true);
        benchmark.start(5, () -> Assert.assertEquals(1, config.getA()));
    }

    @Test
    public void value() {
        ProxySimpleConfigConverter converter = new ProxySimpleConfigConverter(new MockSimpleConfigRepository());
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

    public static class Demo {

        private String a;
        private String b;

        public Demo(String a, String b) {
            this.a = a;
            this.b = b;
        }

        public String getA() {
            return a;
        }

        public Demo setA(String a) {
            this.a = a;
            return this;
        }

        public String getB() {
            return b;
        }

        public Demo setB(String b) {
            this.b = b;
            return this;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Demo demo = (Demo) o;
            return a.equals(demo.a) &&
                    b.equals(demo.b);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    private static class MockSimpleConfigRepository implements SimpleConfigRepository {

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
                c("f", "true")
        );

        @Override
        public List<SimpleConfig> allConfigs() {
            return list;
        }
    }
}