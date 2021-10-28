package org.team4u.base.log.aop;

public class ByteBuddyLogAopTest extends AbstractLogAopTest {

    @Override
    protected LogAop newLogAop() {
        return new ByteBuddyLogAop();
    }
}