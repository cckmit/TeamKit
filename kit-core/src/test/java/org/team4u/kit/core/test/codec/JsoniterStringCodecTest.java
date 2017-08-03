package org.team4u.kit.core.test.codec;

import org.junit.Assert;
import org.junit.Test;
import org.team4u.kit.core.codec.Codec;
import org.team4u.kit.core.codec.impl.JsoniterStringCodec;

/**
 * @author Jay Wu
 */
public class JsoniterStringCodecTest {

    @Test
    public void encodeAndDecode() {
        Codec<A, String> codec = new JsoniterStringCodec<A>() {
        };
        String json = codec.encode(new A().setName("x"));
        A a = codec.decode(json);
        Assert.assertEquals("x", a.name);
    }

    public static class A {

        public String name;

        public String getName() {
            return name;
        }

        public A setName(String name) {
            this.name = name;
            return this;
        }
    }
}
