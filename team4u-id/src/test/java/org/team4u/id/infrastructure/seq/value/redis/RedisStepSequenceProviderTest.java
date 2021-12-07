package org.team4u.id.infrastructure.seq.value.redis;

import cn.hutool.core.bean.BeanUtil;
import org.junit.Before;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.team4u.TestUtil;
import org.team4u.id.domain.seq.value.AbstractStepSequenceProviderTest;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

public class RedisStepSequenceProviderTest extends AbstractStepSequenceProviderTest {

    private final StringRedisTemplate template = TestUtil.createRedisTemplate();

    @Before
    public void setUp() {
        RedisStepSequenceProvider provider = provider();
        template.delete(provider.sequenceIdOf(context()));
    }

    @Override
    protected StepSequenceProvider provider(StepSequenceProvider.Config config) {
        RedisStepSequenceProvider.Config redisConfig = new RedisStepSequenceProvider.Config();
        BeanUtil.copyProperties(config, redisConfig);
        return new RedisStepSequenceProvider(
                redisConfig,
                TestUtil.createRedisTemplate()
        );
    }

    private RedisStepSequenceProvider provider() {
        RedisStepSequenceProvider.Config config = new RedisStepSequenceProvider.Config();
        return new RedisStepSequenceProvider(
                config,
                TestUtil.createRedisTemplate()
        );
    }
}