package org.team4u.ddd.process;

import org.team4u.base.lang.LongTimeThread;
import org.team4u.kv.SimpleLockService;

/**
 * 基于本地线程轮询的超时处理通知者
 *
 * @author jay.wu
 */
public class LocalProcessTimedOutInformer extends LongTimeThread {

    private final Config config;

    private final SimpleLockService lockService;
    private final TimeConstrainedProcessTrackerAppService trackerAppService;

    public LocalProcessTimedOutInformer(Config config,
                                        SimpleLockService lockService,
                                        TimeConstrainedProcessTrackerAppService trackerAppService) {
        this.config = config;
        this.lockService = lockService;
        this.trackerAppService = trackerAppService;

        setName(config.id());
    }

    @Override
    protected void onRun() {
        if (!lockService.tryLock(getName(), config.getMaxTimeoutMillsPerLock())) {
            return;
        }

        try {
            informProcessTimedOut();
        } finally {
            lockService.unLock(getName());
        }
    }

    @Override
    protected Number runIntervalMillis() {
        return config.getRunningIntervalMills();
    }

    private void informProcessTimedOut() {
        if (config.getTimedOutEventClass() == null) {
            trackerAppService.informAllProcessTimedOut(config.getMaxCountPerInform());
        } else {
            trackerAppService.informProcessTimedOut(
                    config.getTimedOutEventClass(),
                    config.getMaxCountPerInform()
            );
        }
    }

    public static class Config {
        /**
         * 运行间隔（毫秒）
         */
        private int runningIntervalMills = 10000;

        /**
         * 锁最大时长（毫秒）
         */
        private int maxTimeoutMillsPerLock = 5000;
        /**
         * 每次最大通知数量
         */
        private int maxCountPerInform = 150;
        /**
         * 超时事件类型
         */
        private Class<ProcessTimedOutEvent> timedOutEventClass;

        public int getRunningIntervalMills() {
            return runningIntervalMills;
        }

        public Config setRunningIntervalMills(int runningIntervalMills) {
            this.runningIntervalMills = runningIntervalMills;
            return this;
        }

        public int getMaxTimeoutMillsPerLock() {
            return maxTimeoutMillsPerLock;
        }

        public Config setMaxTimeoutMillsPerLock(int maxTimeoutMillsPerLock) {
            this.maxTimeoutMillsPerLock = maxTimeoutMillsPerLock;
            return this;
        }

        public int getMaxCountPerInform() {
            return maxCountPerInform;
        }

        public Config setMaxCountPerInform(int maxCountPerInform) {
            this.maxCountPerInform = maxCountPerInform;
            return this;
        }

        public Class<ProcessTimedOutEvent> getTimedOutEventClass() {
            return timedOutEventClass;
        }

        public Config setTimedOutEventClass(Class<ProcessTimedOutEvent> timedOutEventClass) {
            this.timedOutEventClass = timedOutEventClass;
            return this;
        }

        public String id() {
            String id = "tracker_informer";
            if (getTimedOutEventClass() == null) {
                return id;
            }


            return id + "_" + getTimedOutEventClass().getSimpleName();
        }
    }
}