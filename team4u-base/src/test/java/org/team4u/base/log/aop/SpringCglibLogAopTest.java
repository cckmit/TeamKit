package org.team4u.base.log.aop;

public class SpringCglibLogAopTest extends AbstractLogAopTest {

    @Override
    protected LogAop newLogAop() {
        return new SpringCglibLogAop();
    }
}