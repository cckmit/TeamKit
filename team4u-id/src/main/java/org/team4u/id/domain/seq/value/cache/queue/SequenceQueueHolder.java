package org.team4u.id.domain.seq.value.cache.queue;

import lombok.Data;
import lombok.Getter;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.lang.lazy.LazySupplier;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import static org.team4u.id.domain.seq.value.cache.CacheSequenceContextKey.CACHE_STEP_SEQUENCE_CONFIG_KEY;
import static org.team4u.id.domain.seq.value.cache.CacheSequenceContextKey.STEP_SEQUENCE_PROVIDER_KEY;

/**
 * 序号队列服务
 * <p>
 * -负责缓存序号队列和其生产者
 * <p>
 * - 负责启动序号队列清理器
 *
 * @author jay.wu
 * @see SequenceQueueCleaner
 */
public class SequenceQueueHolder {

    private static final LazySupplier<SequenceQueueHolder> instance = LazySupplier.of(SequenceQueueHolder::new);

    public static SequenceQueueHolder getInstance() {
        return instance.get();
    }

    @Getter
    private final SequenceQueueCleaner queueCleaner;

    private final LazyFunction<SequenceProvider.Context, QueueAndProducer> queues;

    public SequenceQueueHolder() {
        queues = LazyFunction.of(buildLazyConfig(), this::buildQueue);
        queueCleaner = buildQueueCleaner();
    }

    private SequenceQueueCleaner buildQueueCleaner() {
        SequenceQueueCleaner queueCleaner = new SequenceQueueCleaner(queues);
        queueCleaner.start();
        return queueCleaner;
    }

    private LazyFunction.Config buildLazyConfig() {
        return LazyFunction.Config.builder()
                .keyFunc(it -> ((SequenceProvider.Context) it).id())
                .build();
    }

    private QueueAndProducer buildQueue(SequenceProvider.Context context) {
        StepSequenceProvider delegateProvider = context.ext(STEP_SEQUENCE_PROVIDER_KEY);

        SequenceQueue queue = new SequenceQueue(context, delegateProvider);

        SequenceQueueProducer producer = new SequenceQueueProducer(
                queue,
                context.ext(CACHE_STEP_SEQUENCE_CONFIG_KEY),
                context.ext(STEP_SEQUENCE_PROVIDER_KEY)
        );
        producer.start();
        return new QueueAndProducer(queue, producer);
    }

    public SequenceQueue queueOf(SequenceProvider.Context context) {
        return queues.apply(context).getQueue();
    }

    public SequenceQueueProducer producerOf(SequenceProvider.Context context) {
        return queues.apply(context).getProducer();
    }

    public void clear() {
        queues.reset();
    }

    @Data
    public static class QueueAndProducer {
        private final SequenceQueue queue;
        private final SequenceQueueProducer producer;
    }
}