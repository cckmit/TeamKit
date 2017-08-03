package org.team4u.kit.core.id;

import java.util.concurrent.atomic.AtomicLong;

public class MemoryLongGenerator implements INumberGenerator {

    private AtomicLong adder;

    private long minValue;
    private long maxValue;

    public MemoryLongGenerator() {
        this(0);
    }

    public MemoryLongGenerator(long minValue) {
        this(minValue, Long.MAX_VALUE);
    }

    public MemoryLongGenerator(long minValue, long maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        adder = new AtomicLong(minValue);
    }

    public Long next() {
        synchronized (this) {
            if (adder.longValue() >= maxValue) {
                reset();
            }
        }

        return adder.getAndIncrement();
    }

    @Override
    public void reset() {
        synchronized (this) {
            adder.set(minValue);
        }
    }
}