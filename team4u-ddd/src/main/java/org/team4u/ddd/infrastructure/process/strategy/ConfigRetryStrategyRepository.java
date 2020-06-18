package org.team4u.ddd.infrastructure.process.strategy;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.team4u.ddd.process.strategy.RetryStrategy;
import org.team4u.ddd.process.strategy.RetryStrategyFactoryService;
import org.team4u.ddd.process.strategy.RetryStrategyRepository;
import org.team4u.core.config.ConfigService;

/**
 * 基于配置的重试策略资源库
 *
 * @author jay.wu
 */
public class ConfigRetryStrategyRepository implements RetryStrategyRepository {

    private final Cache<String, RetryStrategy> idRetryStrategyCache = CacheUtil.newTimedCache(cacheTimeoutSec());

    private final ConfigService configService;
    private final RetryStrategyFactoryService retryStrategyFactoryService;

    public ConfigRetryStrategyRepository(ConfigService configService) {
        this.configService = configService;
        this.retryStrategyFactoryService = new RetryStrategyFactoryService();
    }

    @Override
    public RetryStrategy strategyOf(String id) {
        RetryStrategy strategy = strategyOfCache(id);

        if (strategy == null) {
            // 尝试刷新缓存
            strategy = refreshCache(id);

            if (strategy == null) {
                // 尝试加载默认策略
                return refreshCache(DEFAULT_STRATEGY_ID);
            }
        }

        return strategy;
    }

    /**
     * 获取本地缓存超时时间
     *
     * @return 本地缓存超时时间（秒）
     */
    protected int cacheTimeoutSec() {
        return 10000;
    }

    /**
     * 从配置服务加载策略配置内容
     * <p>
     * key格式为：retry.strategy.$strategyId
     *
     * @param strategyId 策略标识
     * @return 策略配置内容
     */
    protected String loadStrategyConfig(String strategyId) {
        String prefix = "retry.strategy.";
        return configService.get(prefix + strategyId);
    }

    private RetryStrategy refreshCache(String strategyId) {
        String content = loadStrategyConfig(strategyId);
        if (StrUtil.isEmpty(content)) {
            return null;
        }

        JSONObject jsonConfig = JSON.parseObject(content);
        String type = jsonConfig.getString("type");
        return createAndSaveRetryStrategy(type, jsonConfig.toJSONString());
    }

    private RetryStrategy createAndSaveRetryStrategy(String factoryType, String config) {
        RetryStrategy strategy = retryStrategyFactoryService.create(factoryType, config);
        idRetryStrategyCache.put(strategy.strategyId(), strategy);
        return strategy;
    }

    private RetryStrategy strategyOfCache(String id) {
        return idRetryStrategyCache.get(id);
    }
}