package org.team4u.rl.application;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessage;
import org.team4u.rl.domain.*;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 限流器应用服务
 *
 * @author jay.wu
 */
public class RateLimiterAppService extends LongTimeThread implements RateLimiterService {

    protected final Log log = LogFactory.get();

    protected final Config config;
    private final RateLimiterFactory rateLimiterFactory;
    protected RateLimiterConfig rateLimiterConfig;
    private final RateLimitConfigRepository rateLimitConfigRepository;
    private Map<String, RateLimiter> rateLimiters = Collections.emptyMap();

    public RateLimiterAppService(Config config,
                                 RateLimiterFactory rateLimiterFactory,
                                 RateLimitConfigRepository configRepository) {
        this.config = config;
        this.rateLimiterFactory = rateLimiterFactory;
        this.rateLimitConfigRepository = configRepository;

        onRun();
        start();
    }

    @Override
    public boolean tryAcquire(String type, String key) {
        RateLimiter rateLimiter = rateLimiters.get(type);

        // 不存在限流统计器不做拦截
        if (rateLimiter == null) {
            return true;
        }

        return rateLimiter.tryAcquire(key);
    }

    @Override
    public long countAcquired(String type, String key) {
        RateLimiter rateLimiter = rateLimiters.get(type);

        // 不存在限流统计器不做拦截
        if (rateLimiter == null) {
            return 0;
        }

        return rateLimiter.countAcquired(key);
    }

    @Override
    public boolean canAcquire(String type, String key) {
        RateLimiter rateLimiter = rateLimiters.get(type);

        // 不存在限流统计器不做拦截
        if (rateLimiter == null) {
            return true;
        }

        return rateLimiter.canAcquire(key);
    }

    @Override
    protected void onRun() {
        RateLimiterConfig rateLimiterConfig = rateLimitConfigRepository.configOf(config.getConfigId());
        // 判断规则集合是否有变动，若有则重新构建
        if (ObjectUtil.equals(this.rateLimiterConfig, rateLimiterConfig)) {
            return;
        }

        this.rateLimiterConfig = rateLimiterConfig;
        initRateLimitersWithRules();
    }

    private void initRateLimitersWithRules() {
        rateLimiters = rateLimiterConfig.getRules()
                .stream()
                .collect(Collectors.toMap(
                        RateLimiterConfig.Rule::getType,
                        rateLimiterFactory::create
                ));

        log.info(LogMessage.create(this.getClass().getSimpleName(), "initRateLimitersWithRules")
                .success()
                .append("rateLimiters", rateLimiters.size())
                .toString());
    }

    @Override
    protected Number runIntervalMillis() {
        return config.getRefreshConfigIntervalMillis();
    }

    /**
     * 配置类
     */
    public static class Config {
        private int refreshConfigIntervalMillis = 5000;
        /**
         * 配置标识
         */
        private String configId;

        public int getRefreshConfigIntervalMillis() {
            return refreshConfigIntervalMillis;
        }

        public Config setRefreshConfigIntervalMillis(int refreshConfigIntervalMillis) {
            this.refreshConfigIntervalMillis = refreshConfigIntervalMillis;
            return this;
        }

        public String getConfigId() {
            return configId;
        }

        public Config setConfigId(String configId) {
            this.configId = configId;
            return this;
        }
    }
}