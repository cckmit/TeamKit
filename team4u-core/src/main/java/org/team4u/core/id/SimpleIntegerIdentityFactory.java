package org.team4u.core.id;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * int主键生成器
 *
 * @author jay.wu
 */
public class SimpleIntegerIdentityFactory implements NumberIdentityFactory {

    private final AtomicInteger adder;

    private final int minValue;
    private final int maxValue;

    public SimpleIntegerIdentityFactory() {
        this(0);
    }

    public SimpleIntegerIdentityFactory(int minValue) {
        this(minValue, Integer.MAX_VALUE);
    }

    public SimpleIntegerIdentityFactory(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        adder = new AtomicInteger(minValue);
    }

    @Override
    public Number create() {
        synchronized (this) {
            if (adder.longValue() >= maxValue) {
                reset();
            }
        }

        return adder.getAndIncrement();
    }

    public void reset() {
        synchronized (this) {
            adder.set(minValue);
        }
    }
}