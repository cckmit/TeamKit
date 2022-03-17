package org.team4u.id.domain.seq.value;

import cn.hutool.core.util.ObjectUtil;
import org.team4u.base.lang.lazy.LazyFunction;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于内存的序号提供者
 *
 * @author jay.wu
 */
public class InMemoryStepSequenceProvider extends AutoIncrementStepSequenceProvider {

    private final Config config;
    private final LazyFunction<String, AtomicLong> lazyCounters;

    public InMemoryStepSequenceProvider(Config config) {
        this.config = config;
        lazyCounters = LazyFunction.of(
                LazyFunction.Config.builder().name(getClass().getSimpleName() + "|lazyCounters").build(),
                it -> new AtomicLong(0)
        );
    }

    private AtomicLong counterOf(Context context) {
        return lazyCounters.apply(context.getGroupKey());
    }

    @Override
    public Number currentSequence(Context context) {
        return counterOf(context).get();
    }

    @Override
    protected Number addAndGet(Context context) {
        return counterOf(context).addAndGet(config.getStep());
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
            return new InMemoryStepSequenceProvider(ObjectUtil.defaultIfNull(config, new Config()));
        }
    }
}