package org.team4u.id.domain.seq.value.cache;

import lombok.Data;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

/**
 * 缓存号段序号配置
 *
 * @author jay.wu
 */
@Data
public class CacheStepSequenceConfig {
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
     * 获取序号超时时间（毫秒）
     */
    private int nextTimeoutMillis = 20;
    /**
     * 序号队列开启多长时间失效（毫秒）,0则表示永不失效
     */
    private int expiredWhenQueueStartedMillis = 0;

    /**
     * 序号队列是否会过期
     */
    public boolean isQueueWillExpire() {
        return expiredWhenQueueStartedMillis > 0;
    }
}