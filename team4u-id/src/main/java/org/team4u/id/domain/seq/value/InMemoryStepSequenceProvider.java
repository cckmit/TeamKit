package org.team4u.id.domain.seq.value;

import org.team4u.base.lang.lazy.LazyFunction;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于内存的序号提供者
 *
 * @author jay.wu
 */
public class InMemoryStepSequenceProvider implements StepSequenceProvider {

    private final Config config;
    private final LazyFunction<String, AtomicLong> lazyCounters;

    public InMemoryStepSequenceProvider(Config config) {
        this.config = config;
        lazyCounters = LazyFunction.of(it -> new AtomicLong(config.getStart()));
    }

    @Override
    public Number provide(Context context) {
        AtomicLong counter = counterOf(context);

        if (!canUpdateIfOverMaxValue(counter)) {
            return null;
        }

        if (resetIfOverMaxValue(counter)) {
            return counter.get();
        }

        return counter.getAndAdd(config.getStep());
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private boolean resetIfOverMaxValue(AtomicLong counter) {
        if (counter.get() <= config.getMaxValue()) {
            return false;
        }

        synchronized (counter) {
            if (counter.get() > config.getMaxValue()) {
                counter.set(config.getStart());
                return true;
            }
        }

        return false;
    }

    private boolean canUpdateIfOverMaxValue(AtomicLong counter) {
        if (counter.get() < config.getMaxValue()) {
            return true;
        }

        return config.isRecycleAfterMaxValue();
    }

    private AtomicLong counterOf(Context context) {
        return lazyCounters.apply(context.getGroupKey());
    }

    public long currentSeq(Context context) {
        return counterOf(context).get();
    }

    @Override
    public Config config() {
        return config;
    }

    public static class Factory extends AbstractSequenceProviderFactory<Config> {

        @Override
        public String id() {
            return "MS";
        }

        @Override
        protected SequenceProvider createWithConfig(Config config) {
            return new InMemoryStepSequenceProvider(config);
        }
    }
}