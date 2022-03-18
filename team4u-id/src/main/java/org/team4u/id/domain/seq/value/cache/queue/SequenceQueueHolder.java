package org.team4u.id.domain.seq.value.cache.queue;

import lombok.Data;
import lombok.Getter;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.lang.lazy.LazySupplier;
import org.team4u.id.domain.seq.value.SequenceProvider;

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

    private final LazyFunction<SequenceQueueContext, HolderValue> queues;

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
                .keyFunc(it -> ((SequenceQueueContext) it).id())
                .build();
    }

    private HolderValue buildQueue(SequenceQueueContext context) {
        SequenceQueue queue = new SequenceQueue(context);

        SequenceQueueProducer producer = new SequenceQueueProducer(queue, context);
        producer.start();
        return new HolderValue(queue, producer);
    }

    public SequenceQueue queueOf(SequenceQueueContext context) {
        return queues.apply(context).getQueue();
    }

    public SequenceQueueProducer producerOf(SequenceProvider.Context context) {
        HolderValue value = queues.apply(new SequenceQueueContext(
                context,
                null,
                null
        ));

        if (value == null) {
            return null;
        }

        return value.getProducer();
    }

    public void clear() {
        queues.reset();
    }

    @Data
    public static class HolderValue {
        private final SequenceQueue queue;
        private final SequenceQueueProducer producer;
    }
}