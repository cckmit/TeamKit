package org.team4u.base.message.mq;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Test;

public class MessageQueueMapperTest {

    @Test
    public void start() {
        Queue q = new Queue();
        Consumer c = new Consumer();
        MessageQueueMapper mapper = new MessageQueueMapper(c, q, new JsonMessageConverter());
        mapper.start();

        String name = "fjay";
        q.handler.onMessage(JSON.toJSONString(new A().setName(name)));

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
        protected void internalOnMessage(A message) {
            this.a = message;
        }

        public A getA() {
            return a;
        }
    }

    public static class Queue implements MessageQueue<String> {

        private MessageHandler<String> handler;

        @Override
        public void start() {

        }

        @Override
        public void messageHandler(MessageHandler<String> handler) {
            this.handler = handler;
        }

        @Override
        public void close() {

        }
    }
}