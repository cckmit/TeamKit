package org.team4u.sc;

import cn.hutool.core.thread.ThreadUtil;

import static org.team4u.sc.TestUtil.TEST_ID;

public class FakeInvoker implements Invoker {

    private TestDemo testDemo = new TestDemo().setCode(TEST_ID);

    private int delayMillis;

    public FakeInvoker() {
        this(0);
    }

    public FakeInvoker(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    @Override
    public Object invoke() {
        if (delayMillis > 0) {
            ThreadUtil.sleep(delayMillis);
        }

        return testDemo;
    }

    public TestDemo getTestDemo() {
        return testDemo;
    }

    public FakeInvoker setTestDemo(TestDemo testDemo) {
        this.testDemo = testDemo;
        return this;
    }
}
