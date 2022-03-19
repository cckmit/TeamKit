package org.team4u.id.domain.seq.value;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于内存的序号提供者
 *
 * @author jay.wu
 */
public class InMemoryStepSequenceProvider extends AutoIncrementStepSequenceProvider {

    private final Config config;
    private final AtomicLong counter;

    public InMemoryStepSequenceProvider(Config config) {
        this.config = config;
        this.counter = new AtomicLong(0);
    }

    @Override
    public Number currentSequence(Context context) {
        return counter.get();
    }

    @Override
    protected Number addAndGet(Context context) {
        return counter.addAndGet(config.getStep());
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