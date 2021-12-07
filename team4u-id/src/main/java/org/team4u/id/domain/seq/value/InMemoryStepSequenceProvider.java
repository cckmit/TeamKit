package org.team4u.id.domain.seq.value;

import cn.hutool.log.Log;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.log.LogMessage;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于内存的序号提供者
 *
 * @author jay.wu
 */
public class InMemoryStepSequenceProvider implements StepSequenceProvider {

    private final Log log = Log.get();

    private final Config config;
    private final LazyFunction<String, AtomicLong> lazyCounters;

    public InMemoryStepSequenceProvider(Config config) {
        this.config = config;
        lazyCounters = LazyFunction.of(it -> new AtomicLong(config.getStart()));
    }

    @Override
    public Number provide(Context context) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "provide")
                .append("context", context);

        AtomicLong counter = counterOf(context);

        if (!canUpdateIfOverMaxValue(counter)) {
            log.info(lm.fail().append("canUpdateIfOverMaxValue", false).toString());
            return null;
        }

        Long newValue = resetIfOverMaxValue(counter.getAndAdd(config.getStep()));
        log.info(lm.append("currentValue", newValue).success().toString());
        return newValue;
    }

    private boolean canUpdateIfOverMaxValue(AtomicLong counter) {
        if (counter.get() <= config.getMaxValue()) {
            return true;
        }

        return config.isRecycleAfterMaxValue();
    }

    private Long resetIfOverMaxValue(Long value) {
        if (value <= config.getMaxValue()) {
            return value;
        }

        // 循环使用
        if (config.isRecycleAfterMaxValue()) {
            return config.valueWithRecycle(value).longValue();
        }

        return config.getMaxValue();
    }

    private AtomicLong counterOf(Context context) {
        return lazyCounters.apply(context.getGroupKey());
    }

    /**
     * 当前序号值
     *
     * @param context 上下文
     */
    public Number currentSequence(Context context) {
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