package org.team4u.ddd.message;

import cn.hutool.core.lang.func.VoidFunc1;
import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;
import org.team4u.ddd.infrastructure.message.JsonMessageConverter;

import java.io.IOException;

public class MessageQueueMapperTest {

    @Test
    public void start() {
        Queue q = new Queue();
        Consumer c = new Consumer();
        MessageQueueMapper mapper = new MessageQueueMapper(c, q, new JsonMessageConverter());
        mapper.start();

        String name = "fjay";
        q.handler.callWithRuntimeException(JSON.toJSONString(new A().setName(name)));

        Assert.assertEquals(name, c.getA().getName());
    }

    public static class A {
        private String name;

        public String getName() {
            return name;
        }

        public A setName(String name) {
            this.name = name;
            return this;
        }
    }

    public static class Consumer extends AbstractMessageConsumer<A> {

        private A a;

        @Override
        protected void internalOnMessage(A message) throws Throwable {
            this.a = message;
        }

        public A getA() {
            return a;
        }
    }

    public static class Queue implements MessageQueue<String> {

        private VoidFunc1<String> handler;

        @Override
        public void start() {

        }

        @Override
        public void messageHandler(VoidFunc1<String> handler) {
            this.handler = handler;
        }

        @Override
        public void close() throws IOException {

        }
    }
}