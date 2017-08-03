package org.team4u.kit.core.id;

import java.util.concurrent.atomic.AtomicInteger;

public class MemoryIntegerGenerator implements INumberGenerator {

    private AtomicInteger adder;

    private int minValue;
    private int maxValue;

    public MemoryIntegerGenerator() {
        this(0);
    }

    public MemoryIntegerGenerator(int minValue) {
        this(minValue, Integer.MAX_VALUE);
    }

    public MemoryIntegerGenerator(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        adder = new AtomicInteger(minValue);
    }

    public Integer next() {
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