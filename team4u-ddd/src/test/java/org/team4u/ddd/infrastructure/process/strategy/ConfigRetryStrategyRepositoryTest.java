package org.team4u.ddd.infrastructure.process.strategy;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.team4u.base.config.ConfigService;
import org.team4u.ddd.TestUtil;
import org.team4u.ddd.process.strategy.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigRetryStrategyRepositoryTest {

    @Mock
    private ConfigService configService;

    private ConfigRetryStrategyRepository configRetryStrategyRepository;

    @Before
    public void setUp() {
        configRetryStrategyRepository = new ConfigRetryStrategyRepository(configService);
    }

    @Test
    public void noRetryStrategy() {
        RetryStrategy s = configRetryStrategyRepository.strategyOf(TestUtil.TEST_ID);
        Assert.assertNull(s);
    }

    @Test
    public void defaultRetryStrategy() {
        loadDefaultConfig();

        String id = "fixed2";
        loadConfig(id);
        RetryStrategy s = configRetryStrategyRepository.strategyOf(id);

        checkBaseConfig(RetryStrategyRepository.DEFAULT_STRATEGY_ID, s);

        Assert.assertTrue(s instanceof FixedRetryStrategy);
        Assert.assertEquals(1, ((FixedRetryStrategy) s).getConfig().getIntervalSec());
    }

    @Test
    public void fixedRetryStrategy() {
        String id = "fixed";

        loadConfig(id);

        RetryStrategy s = configRetryStrategyRepository.strategyOf(id);

        checkBaseConfig(id, s);

        Assert.assertTrue(s instanceof FixedRetryStrategy);
        Assert.assertEquals(1, ((FixedRetryStrategy) s).getConfig().getIntervalSec());
    }

    @Test
    public void increment() {
        String id = "increment";

        loadConfig(id);

        RetryStrategy s = configRetryStrategyRepository.strategyOf(id);

        checkBaseConfig(id, s);

        Assert.assertTrue(s instanceof IncrementRetryStrategy);
        Assert.assertEquals(1, ((IncrementRetryStrategy) s).getConfig().getIntervalSec());
    }

    @Test
    public void exponentialRandom2() {
        String id = "exponentialRandom2";

        loadConfig(id);

        RetryStrategy s = configRetryStrategyRepository.strategyOf(id);

        checkBaseConfig(id, s);

        Assert.assertTrue(s instanceof ExponentialRandom2RetryStrategy);
        Assert.assertEquals(1, ((ExponentialRandom2RetryStrategy) s).getConfig().getExponentialBase());
    }

    @Test
    public void exponential() {
        String id = "exponential";

        loadConfig(id);

        RetryStrategy s = configRetryStrategyRepository.strategyOf(id);

        checkBaseConfig(id, s);

        Assert.assertTrue(s instanceof ExponentialRetryStrategy);
        ExponentialRetryStrategy.Config config = ((ExponentialRetryStrategy) s).getConfig();
        Assert.assertEquals(2, config.getExponentialBase());
    }

    @Test
    public void exponentialRandom() {
        String id = "exponentialRandom";

        loadConfig(id);

        RetryStrategy s = configRetryStrategyRepository.strategyOf(id);

        checkBaseConfig(id, s);

        Assert.assertTrue(s instanceof ExponentialRandomRetryStrategy);
        ExponentialRandomRetryStrategy.Config config = ((ExponentialRandomRetryStrategy) s).getConfig();
        Assert.assertEquals(3, config.getExponentialBase());
        Assert.assertEquals(1, config.getMinIntervalSec());
    }

    private void checkBaseConfig(String id, RetryStrategy s) {
        Assert.assertEquals(id, s.strategyId());
        Assert.assertEquals(1, s.maxRetriesPermitted());

    }

    private void loadConfig(String id) {
        String content = TestUtil.readFile("process/testRetryStrategy.json");
        String config = JSON.parseObject(content).getString(id);
        Mockito.when(configService.get("retry.strategy." + id)).thenReturn(config);
    }

    private void loadDefaultConfig() {
        loadConfig(ConfigRetryStrategyRepository.DEFAULT_STRATEGY_ID);
    }
}