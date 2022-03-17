package org.team4u.base.log.aop;

import org.junit.Assert;
import org.junit.Test;

public class LogAopServiceTest {


    @Test
    public void byteBuddyLogAop() {
        A a = LogAopService.getInstance().proxy(
                new A(),
                LogAop.Config.builder().logAopId(ByteBuddyLogAop.ID).build()
        );
        a.say("x");
        Assert.assertTrue(a.getClass().getSimpleName().contains(ByteBuddyLogAop.ID));
    }

    @Test
    public void springCglibLogAop() {
        A a = LogAopService.getInstance().proxy(new A());
        a.say("x");
        Assert.assertTrue(a.getClass().getSimpleName().contains("CGLIB"));
    }

    public static class A {

        public void say(String name) {
        }
    }
}