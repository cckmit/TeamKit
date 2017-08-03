package org.team4u.kit.core.id;

import java.util.concurrent.atomic.AtomicLong;

public class MemoryMillisLongGenerator implements INumberGenerator {

    private AtomicLong adder;
    private String format;
    private Long maxValue;

    public MemoryMillisLongGenerator(int capacity) {
        adder = new AtomicLong();
        format = "%s%0" + capacity + "d";
        maxValue = Long.valueOf(String.format(format, 1, 0));
    }

    public MemoryMillisLongGenerator() {
        this(5);
    }

    public Long next() {
        synchronized (this) {
            if (adder.longValue() >= maxValue) {
                reset();
            }
        }

        return Long.valueOf(String.format(format, System.currentTimeMillis(), adder.getAndIncrement()));
    }

    @Override
    public void reset() {
        adder.set(0L);
    }
}