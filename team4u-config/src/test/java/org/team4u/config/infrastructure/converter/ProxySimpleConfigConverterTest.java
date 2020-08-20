package org.team4u.config.infrastructure.converter;

import cn.hutool.core.collection.CollUtil;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.config.TestUtil;
import org.team4u.config.domain.SimpleConfig;
import org.team4u.config.domain.SimpleConfigRepository;

import java.util.List;
import java.util.Map;

import static org.team4u.config.TestUtil.c;

public class ProxySimpleConfigConverterTest {

    @Test
    public void type() {
        ProxySimpleConfigConverter converter = new ProxySimpleConfigConverter(new MockSimpleConfigRepository());
        TestConfig config = converter.to(
                TestConfig.class,
                TestUtil.TEST_ID
        );

        Assert.assertEquals(1, config.getA());
        Assert.assertArrayEquals(new String[]{"1", "2"}, config.getB());
        Assert.assertEquals(CollUtil.newArrayList(1, 2), config.getC());
        Assert.assertEquals("1", config.getD().get("test"));
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
    }

    public static class TestConfig {

        private int a;
        private String[] b;
        private List<Integer> c;
        private Map<String, String> d;

        public int getA() {
            return a;
        }

        public String[] getB() {
            return b;
        }

        public void setB(String[] b) {
            this.b = b;
        }

        public List<Integer> getC() {
            return c;
        }

        public void setC(List<Integer> c) {
            this.c = c;
        }

        public Map<String, String> getD() {
            return d;
        }

        public void setD(Map<String, String> d) {
            this.d = d;
        }
    }

    private class MockSimpleConfigRepository implements SimpleConfigRepository {

        @Override
        public List<SimpleConfig> allConfigs() {
            return CollUtil.newArrayList(
                    c("a", "1"),
                    c("b", "1,2"),
                    c("c", "1,2"),
                    c("d", "{\"test\":1}")
            );
        }
    }
}