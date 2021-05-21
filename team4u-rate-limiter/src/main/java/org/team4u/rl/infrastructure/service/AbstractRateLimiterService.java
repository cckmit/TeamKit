package org.team4u.rl.infrastructure.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.team4u.base.lang.LongTimeThread;
import org.team4u.base.log.LogMessage;
import org.team4u.rl.domain.RateLimitConfigRepository;
import org.team4u.rl.domain.RateLimiter;
import org.team4u.rl.domain.RateLimiterConfig;
import org.team4u.rl.domain.RateLimiterService;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 计数限流器管理者
 *
 * @author jay.wu
 */
public abstract class AbstractRateLimiterService extends LongTimeThread implements RateLimiterService {

    protected final Log log = LogFactory.get();
    private final RateLimitConfigRepository rateLimitConfigRepository;
    protected Config config;
    protected RateLimiterConfig rateLimiterConfig;
    private Map<String, RateLimiter> rateLimiters = Collections.emptyMap();

    public AbstractRateLimiterService(Config config, RateLimitConfigRepository configRepository) {
        this.config = config;
        this.rateLimitConfigRepository = configRepository;
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

    /**
     * 创建限流器
     */
    protected abstract RateLimiter createRateLimiter(RateLimiterConfig.Rule rule);

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
                        this::createRateLimiter
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