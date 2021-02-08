package org.team4u.base.lang;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Assert;
import org.junit.Test;

public class ManuallyLongTimeThreadTest {

    @Test
    public void notEnabled() {
        TestThread t = new TestThread(false, 1);
        t.start();
        ThreadUtil.sleep(100);
        Assert.assertEquals(0, t.getRunCount());
    }

    @Test
    public void runTwice() {
        TestThread t = new TestThread(true, 2);
        t.start();
        ThreadUtil.sleep(100);
        Assert.assertEquals(2, t.getRunCount());
    }

    private static class TestThread extends ManuallyLongTimeThread {

        private final boolean isEnabled;
        private final int maxRunCount;

        private TestThread(boolean isEnabled, int maxRunCount) {
            this.isEnabled = isEnabled;
            this.maxRunCount = maxRunCount;
        }

        @Override
        protected void onRun() {

        }

        @Override
        protected Number runIntervalMillis() {
            return 1;
        }

        @Override
        protected boolean isEnabled() {
            return isEnabled;
        }

        @Override
        protected int maxRunCount() {
            return maxRunCount;
        }
    }
}