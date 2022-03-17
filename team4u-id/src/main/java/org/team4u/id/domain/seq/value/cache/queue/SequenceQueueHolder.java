package org.team4u.id.domain.seq.value.cache.queue;

import lombok.Data;
import lombok.Getter;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.lang.lazy.LazySupplier;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import static org.team4u.id.domain.seq.value.cache.queue.CacheSequenceContextKey.CACHE_STEP_SEQUENCE_CONFIG_KEY;
import static org.team4u.id.domain.seq.value.cache.queue.CacheSequenceContextKey.STEP_SEQUENCE_PROVIDER_KEY;

/**
 * 序号队列服务
 *
 * @author jay.wu
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
        queues = buildQueues();
        queueCleaner = buildQueueCleaner();
    }

    private SequenceQueueCleaner buildQueueCleaner() {
        SequenceQueueCleaner queueCleaner = new SequenceQueueCleaner(queues);
        queueCleaner.start();
        return queueCleaner;
    }

    private LazyFunction<SequenceProvider.Context, QueueAndProducer> buildQueues() {
        return LazyFunction.of(buildLazyConfig(), this::buildQueue);
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

    public void clear() {
        queues.reset();
    }

    @Data
    public static class QueueAndProducer {
        private final SequenceQueue queue;
        private final SequenceQueueProducer producer;
    }
}