package org.team4u.test;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Counter {

    private static final Log log = LogFactory.get();
    private final AtomicLong i = new AtomicLong();
    private final Timer timer;

    private final String name;

    private final boolean enable;

    private final int period;

    public Counter(String name, boolean enable, int watchPeriod) {
        this.enable = enable;
        this.name = name;
        this.period = watchPeriod;

        timer = new Timer("counterWatcher");
    }

    public Counter(String name, boolean enable) {
        this(name, enable, 10);
    }

    public Counter count() {
        if (enable) {
            i.incrementAndGet();
        }

        return this;
    }

    public Counter watch() {
        if (enable) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    dump();
                }
            }, TimeUnit.SECONDS.toMillis(period), TimeUnit.SECONDS.toMillis(period));
        }

        return this;
    }

    public long size() {
        return i.get();
    }

    public void stopWatch() {
        if (enable) {
            timer.cancel();
            dump();
        }
    }

    public void reset() {
        if (enable) {
            i.set(0);
        }
    }

    private void dump() {
        if (log.isTraceEnabled()) {
            log.trace("{} size:{}", name, size());
        }
    }
}