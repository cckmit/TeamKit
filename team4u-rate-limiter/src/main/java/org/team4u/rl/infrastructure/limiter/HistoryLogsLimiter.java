package org.team4u.rl.infrastructure.limiter;

import cn.hutool.core.collection.CollUtil;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.domain.RateLimiterFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于历史数据限流器
 *
 * @author jay.wu
 */
public class HistoryLogsLimiter implements RateLimiter {

    private static final ThreadLocal<List<Long>> DATASOURCE = new ThreadLocal<>();
    private final RateLimiterConfig config;

    public HistoryLogsLimiter(RateLimiterConfig config) {
        this.config = config;
    }

    /**
     * 获取上下文数据源
     */
    public static List<Long> getDatasource() {
        return HistoryLogsLimiter.DATASOURCE.get();
    }

    /**
     * 设置上下文数据源
     *
     * @param datasource 数据源
     */
    public static void setDatasource(List<Long> datasource) {
        HistoryLogsLimiter.DATASOURCE.set(datasource);
    }

    /**
     * 移除上下文数据源
     */
    public static void removeDatasource() {
        HistoryLogsLimiter.DATASOURCE.remove();
    }

    @Override
    public synchronized boolean tryAcquire(String key) {
        List<Long> logs = DATASOURCE.get();

        if (logs == null) {
            logs = new ArrayList<>();
            DATASOURCE.set(logs);
        }

        logs.add(System.currentTimeMillis());
        return countTryAcquireTimes(key) <= config.getThreshold();
    }

    @Override
    public long countTryAcquireTimes(String key) {
        List<Long> datasource = HistoryLogsLimiter.DATASOURCE.get();
        if (CollUtil.isEmpty(datasource)) {
            return 0;
        }

        return HistoryLogsLimiter.DATASOURCE.get()
                .stream()
                .filter(it -> System.currentTimeMillis() - it <= config.getExpirationMillis())
                .count();
    }

    @Override
    public boolean canAcquire(String key) {
        return countTryAcquireTimes(key) < config.getThreshold();
    }

    @Override
    public RateLimiterConfig config() {
        return config;
    }

    public static class Factory implements RateLimiterFactory {
        @Override
        public RateLimiter create(RateLimiterConfig config) {
            return new HistoryLogsLimiter(config);
        }
    }
}