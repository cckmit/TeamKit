package org.team4u.id.domain.seq.value;

import cn.hutool.log.Log;
import lombok.Data;
import lombok.Getter;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.error.NestedException;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.lang.lazy.LazyFunction;
import org.team4u.base.log.LogMessage;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 基于缓存的趋势增长序列服务
 * <p>
 * 主要职责：
 * - 负责维护不同类型的计数器，确保不同类型的计数器不会相互干扰
 * - 每个计数器从代理序号提供者中获取一个范围号段，缓存到本地
 * - 当客户端需生成序号时，先使用缓存序号，当缓存序号不足时，再从代理提供者重新获取号段，如此往复
 * - 注意：为了提升性能，一个序号类型将启用一个线程进行异步处理，需要关注线程（类型）数量
 *
 * @author jay.wu
 */
public class CacheStepSequenceProvider implements SequenceProvider {

    private final Log log = Log.get();

    private final Config config;
    /**
     * 代理序号提供者，负责提供真正的序号
     */
    @Getter
    private final StepSequenceProvider delegateProvider;
    /**
     * QueueSequenceProvider缓存池
     */
    private final LazyFunction<Context, QueueSequenceProvider> lazyProviders = LazyFunction.of(
            LazyFunction.Config.builder().keyFunc(it -> ((Context) it).id()).build(),
            QueueSequenceProvider::new
    );

    /**
     * @param config           配置
     * @param delegateProvider 代理序号提供者，负责提供真正的序号
     */
    public CacheStepSequenceProvider(Config config, StepSequenceProvider delegateProvider) {
        this.config = config;
        this.delegateProvider = delegateProvider;

        if (config.shouldStartClearWorker()) {
            // 启动定时清理器
            new ClearWorker().start();
        }
    }

    @Override
    public Number provide(Context context) {
        // 根据不同上下文，获取专属的QueueSequenceProvider
        // 同一个上下文只会创建一次
        return lazyProviders.apply(context).next();
    }

    @Override
    public boolean isEmpty(Context context) {
        return lazyProviders.apply(context).isEmpty();
    }

    /**
     * 清理过期缓存
     *
     * @return 已清理数量
     */
    public int clearExpiredCache() {
        return lazyProviders.remove(it -> {
            // 已过期，返回需要清理的key
            if (it.isExpired()) {
                return it.getContext();
            }

            return null;
        });
    }

    public Config config() {
        return config;
    }

    /**
     * 基于队列的序号提供者
     * <p>
     * 主要职责：
     * - 启动一个线程，异步刷新缓冲计数器，并从其中获取可用序号offer到队列，所有操作在同一个线程内完成，无需考虑并发问题
     * - 客户端获取序号时，直接从队列中poll即可，能获得较好的性能
     */
    private class QueueSequenceProvider extends LongTimeThread {
        /**
         * 是否无可用序号
         */
        @Getter
        private boolean empty = false;

        @Getter
        private final Context context;
        /**
         * 无可用序列时返回此值
         * <p>
         * 队列无法offer null值，使用其他值代替该含义
         */
        private final Number EMPTY_NUMBER = -1;
        /**
         * 线程关闭时间戳，0为未关闭
         */
        private long closedTime = 0;
        /**
         * 号段
         */
        private final Segment segment = new Segment();
        /**
         * 缓存队列
         * <p>
         * 队列大小为代理序号提供者步长，即号段长度
         */
        private final BlockingQueue<Number> cache = new LinkedBlockingQueue<>(delegateProvider.config().getStep());

        private QueueSequenceProvider(Context context) {
            this.context = context;

            // 创建时即进行预热，尽可能避免无法获取序号的情况
            onRun();

            start();
        }

        /**
         * 从队列取出下一个序号
         */
        public Number next() {
            // 已无可用序号，直接跳过，避免无效等待时间
            if (isEmpty()) {
                return null;
            }

            try {
                // 增加等待时间，避免消费速度过快，生产不足的情况
                Number result = cache.poll(config.getNextTimeoutMillis(), TimeUnit.MILLISECONDS);

                // 无序号可用，直接返回null
                if (Objects.equals(result, EMPTY_NUMBER)) {
                    setEmpty();
                    return null;
                }

                return result;
            } catch (InterruptedException e) {
                throw new NestedException(e);
            }
        }

        private void setEmpty() {
            empty = true;
        }

        @Override
        protected void onRun() {
            // 当前号段已耗尽，尝试获取新号段
            if (segment.isEmpty()) {
                refreshSegment();
            }

            if (!offerAllCurrentSegmentSequences()) {
                // 无可用序号，关闭线程
                close();
            }
        }

        /**
         * 更新号段
         * <p>
         * 所有操作均在同一线程调用，无需考虑并发问题
         */
        private void refreshSegment() {
            LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "refreshSegment");

            // 从代理序号提供者获取下一个批次的号段
            Number seq = delegateProvider.provide(context);
            // 更新号段
            segment.refreshSegment(seq);

            if (log.isInfoEnabled()) {
                log.info(lm.success()
                        .append("context", context)
                        .append("delegateSeq", seq)
                        .append("segment", segment)
                        .toString());
            }
        }

        /**
         * 将当前号段所有序号放入队列
         *
         * @return 是否有号段入队列
         */
        private boolean offerAllCurrentSegmentSequences() {
            do {
                Number seq = segment.next();

                try {
                    // 已无可用序号，退出
                    if (seq == null) {
                        offerEmptyNumber();
                        return false;
                    }

                    // 若队列中缓存序号已满，将阻塞线程
                    cache.put(seq);
                } catch (InterruptedException e) {
                    return false;
                }
            } while (!segment.isEmpty());

            return true;
        }

        /**
         * put无法设置null值，当无可用序号时，设置特殊值代替
         */
        private void offerEmptyNumber() throws InterruptedException {
            cache.put(EMPTY_NUMBER);
        }

        @Override
        protected Number runIntervalMillis() {
            // 通过队列的put进行阻塞，故无需设置循环间隔
            return 0;
        }

        @Override
        protected void onClose() {
            closedTime = System.currentTimeMillis();
        }

        public boolean isExpired() {
            // 未关闭线程，或者永不过期，直接返回
            if (!isClosed() || !config.shouldExpiredWhenClose()) {
                return false;
            }

            // 已关闭线程，判断是否超出存活时间
            return System.currentTimeMillis() - closedTime > config.getExpiredWhenCloseMillis();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    /**
     * 缓冲计数器
     * <p>
     * 负责缓存号段，并记录序号使用情况
     */
    private class Segment {
        /**
         * 当前序列
         */
        private Long currentSeq;
        /**
         * 当前号段（不包含）
         */
        private Long currentSegment;
        /**
         * 下一号段（不包含）
         */
        private Long nextSegment;

        public void refreshSegment(Number seq) {
            if (seq == null) {
                return;
            }

            // 更新下一个缓存最大值
            refreshNextSegment(seq);

            // 如果当前号段已耗尽，需重新初始化
            handleIfEmpty();
        }

        private void refreshNextSegment(Number value) {
            nextSegment = value.longValue() + delegateProvider.config().getStep();
        }

        /**
         * 当前号段是否已耗尽
         */
        public boolean isEmpty() {
            if (currentSeq == null) {
                return true;
            }

            if (currentSeq >= currentSegment) {
                return true;
            }

            return currentSeq > delegateProvider.config().getMaxValue();
        }

        public Long next() {
            if (isEmpty()) {
                return null;
            }

            Long result = currentSeq;
            currentSeq += config.getCacheStep();

            return result;
        }

        private void handleIfEmpty() {
            // 当前缓存号段未用完，无需处理
            if (!isEmpty()) {
                return;
            }

            // 下一个号段不可用，重置seq
            if (!isNextBufferAvailable()) {
                resetCurrentSeq();
                return;
            }

            // 尝试用下一个号段
            refreshCurrentSegment();
            refreshCurrentSeqBySegment();

            // 若仍然超出，重置seq
            if (isEmpty()) {
                resetCurrentSeq();
            }
        }

        /**
         * 下一个号段是否可用
         */
        private boolean isNextBufferAvailable() {
            if (nextSegment == null) {
                return false;
            }

            if (currentSegment == null) {
                return true;
            }

            // 循环序号，永不耗尽
            if (delegateProvider.config().isRecycleAfterMaxValue()) {
                return true;
            }

            return !nextSegment.equals(currentSegment);
        }

        private void resetCurrentSeq() {
            currentSeq = null;
        }

        private void refreshCurrentSeqBySegment() {
            currentSeq = currentSegment - delegateProvider.config().getStep();
        }

        private void refreshCurrentSegment() {
            currentSegment = nextSegment;
        }

        @Override
        public String toString() {
            return String.format(
                    "currentSeq:%s,currentSegment:%s,nextSegment:%s",
                    currentSeq,
                    currentSegment,
                    nextSegment
            );
        }
    }

    /**
     * 过期缓存清理器
     * <p>
     * - 职责：清理内存中的过期计数器
     * - 原因：缓存中的计数器会不断增加，若不及时清理会导致内存溢出（如按照 小时 分组，每个小时将生成一条记录）
     */
    private class ClearWorker extends LongTimeThread {

        @Override
        protected void onRun() {
            LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "clearExpiredCache");

            try {
                int count = clearExpiredCache();
                lm.append("count", count);
            } catch (Exception e) {
                log.info(lm.fail(e.getMessage()).toString());
            }

            log.info(lm.success().toString());
        }

        @Override
        protected Number runIntervalMillis() {
            return config.getClearExpiredRunIntervalMillis();
        }
    }

    public static class Factory extends AbstractSequenceProviderFactory<Config> {

        @Override
        public String id() {
            return "CS";
        }

        @Override
        protected SequenceProvider createWithConfig(Config config) {
            StepSequenceProvider delegateProvider = delegateProvider(config);
            return new CacheStepSequenceProvider(config, delegateProvider);
        }

        private StepSequenceProvider delegateProvider(Config config) {
            return holder().create(
                    config.getDelegateId(),
                    config.getDelegateConfig()
            );
        }

        private SequenceProviderFactoryHolder holder() {
            return BeanProviders.getInstance().getBean(SequenceProviderFactoryHolder.class);
        }
    }

    @Data
    public static class Config {
        /**
         * 缓存步进
         */
        private Long cacheStep = 1L;
        /**
         * 最小可用缓存百分比
         * <p>
         * 小于该百分比则异步刷新缓冲池
         */
        private int minAvailableSeqPercent = 60;
        /**
         * 代理序号提供者工厂标识
         *
         * @see StepSequenceProvider.Factory
         */
        private String delegateId;
        /**
         * 代理序号提供者配置
         */
        private String delegateConfig;
        /**
         * 获取序号超时时间（毫秒）
         */
        private int nextTimeoutMillis = 20;
        /**
         * 计数器关闭后多长时间失效（毫秒）,0则表示永不失效
         */
        private int expiredWhenCloseMillis = (int) TimeUnit.HOURS.toMillis(25);
        /**
         * 清理失效缓存间隔（毫秒），0则表示不清理
         */
        private int clearExpiredRunIntervalMillis = (int) TimeUnit.MINUTES.toMillis(1);

        /**
         * 是否启动清理器
         */
        public boolean shouldStartClearWorker() {
            return clearExpiredRunIntervalMillis > 0;
        }

        /**
         * 是否需要在关闭后设置过期
         */
        public boolean shouldExpiredWhenClose() {
            return expiredWhenCloseMillis > 0;
        }
    }
}