package org.team4u.id.domain.seq.value.cache.queue;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import lombok.Getter;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.lang.lazy.LazySupplier;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;

import java.util.List;
import java.util.Optional;

/**
 * 序号队列服务
 * <p>
 * -负责缓存序号队列和其生产者，相同的Context标识，仅初始化一次
 * <p>
 * - 负责启动序号队列清理器，定时清理过期队列
 * <p>
 * - 注意：当缓存配置发生变化时，需要等待下一个group周期才生效
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

    private final LazyFunction<SequenceQueueContext, Value> queues;

    public SequenceQueueHolder() {
        queues = LazyFunction.of(buildLazyConfig(), this::buildQueue);
        queueCleaner = buildQueueCleaner();
    }

    private SequenceQueueCleaner buildQueueCleaner() {
        SequenceQueueCleaner queueCleaner = new SequenceQueueCleaner(this);
        queueCleaner.start();
        return queueCleaner;
    }

    private LazyFunction.Config buildLazyConfig() {
        return LazyFunction.Config.builder()
                .name(this.getClass().getSimpleName())
                .keyFunc(it -> ((SequenceQueueContext) it).id())
                .build();
    }

    private Value buildQueue(SequenceQueueContext context) {
        SequenceQueue queue = new SequenceQueue(context);

        SequenceQueueProducer producer = new SequenceQueueProducer(queue, context);
        producer.start();

        return new Value(context.getCacheConfig(), queue, producer);
    }

    public SequenceQueue queueOf(SequenceQueueContext context) {
        return queues.apply(context).getQueue();
    }

    public SequenceQueueProducer producerOf(SequenceQueueContext context) {
        return Optional.ofNullable(queues.value(context))
                .map(Value::getProducer)
                .orElse(null);
    }

    public List<Value> values() {
        return queues.values();
    }

    public void remove(SequenceQueueContext context) {
        // 关闭队列生产者
        IoUtil.close(producerOf(context));
        // 移除队列
        queues.remove(context);
    }

    public void reset() {
        queues.reset();
    }

    public int size() {
        return queues.size();
    }

    /**
     * 刷新配置
     * <p>
     * 当有新配置时，通过调整旧配置队列过期时间实现快速过期
     *
     * @param newCacheConfig 新增配置
     */
    public void refresh(CacheStepSequenceConfig newCacheConfig) {
        values().stream()
                .filter(it -> StrUtil.equals(it.getCacheConfig().getConfigId(), newCacheConfig.getConfigId()))
                .filter(it -> !it.getCacheConfig().equals(newCacheConfig))
                .forEach(it -> it.getCacheConfig().resetQueueExpiredMillisWhenConfigChanged());
    }

    @Data
    public static class Value {
        private final CacheStepSequenceConfig cacheConfig;
        private final SequenceQueue queue;
        private final SequenceQueueProducer producer;
    }
}