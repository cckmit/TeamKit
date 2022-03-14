package org.team4u.id.domain.seq.value.cache;

import lombok.Data;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import java.util.concurrent.TimeUnit;

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
     * 序号队列关闭后多长时间失效（毫秒）,0则表示永不失效
     */
    private int expiredWhenQueueExhaustedMillis = (int) TimeUnit.HOURS.toMillis(25);
    /**
     * 序号队列清理器运行间隔（毫秒），0则表示不运行
     */
    private int queueCleanerRunIntervalMillis = (int) TimeUnit.MINUTES.toMillis(1);

    /**
     * 是否启动清理器
     */
    public boolean shouldStartClearWorker() {
        return queueCleanerRunIntervalMillis > 0;
    }

    /**
     * 是否需要在序号队列耗尽后设置过期
     */
    public boolean shouldExpiredWhenQueueExhausted() {
        return expiredWhenQueueExhaustedMillis > 0;
    }
}