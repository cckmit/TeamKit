package org.team4u.id.domain.seq.value.cache;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.config.IdentifiedConfig;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

/**
 * 缓存号段序号配置
 *
 * @author jay.wu
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CacheStepSequenceConfig extends IdentifiedConfig {
    /**
     * 缓存步进
     */
    private Long cacheStep = 1L;
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
     * 代理序号提供者配置对象
     */
    private StepSequenceProvider.Config delegateConfigBean;
    /**
     * 最大缓存序号数量
     * <p>
     * 默认使用delegateConfig.step值
     */
    private int maxCacheSeqSize;
    /**
     * 获取序号超时时间（毫秒）
     */
    private int nextTimeoutMillis = 300;

    /**
     * 队列生产者推送阻塞超时时间（毫秒）
     */
    private int queueOfferTimeoutMillis = 60000;
    /**
     * 队列正常失效时间（毫秒）
     * <p>
     * - 0则表示永不失效
     * <p>
     * - 建议与group周期一致
     */
    private int queueExpiredMillis = 0;
    /**
     * 当配置更新后队列失效时间（毫秒）
     * <p>
     * 0则表示更新不失效，将等待正常失效
     */
    private int queueExpiredWhenConfigChangedMillis = 2000;

    /**
     * 序号队列是否会过期
     */
    public boolean isQueueWillExpire() {
        return queueExpiredMillis > 0;
    }

    /**
     * 序号队列是否会过期
     */
    public boolean isQueueWillExpireWhenConfigChanged() {
        return queueExpiredMillis > 0;
    }

    public int maxCacheSeqSize() {
        if (getMaxCacheSeqSize() <= 0) {
            return delegateConfigBean.getStep();
        }

        return getMaxCacheSeqSize();
    }

    public void resetQueueExpiredMillisWhenConfigChanged() {
        if (!isQueueWillExpireWhenConfigChanged()) {
            return;
        }

        // 为了防止旧队列仍然被并发访问，需要延迟过期
        setQueueExpiredMillis(getQueueExpiredWhenConfigChangedMillis());
    }
}