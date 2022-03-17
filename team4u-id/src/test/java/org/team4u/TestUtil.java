package org.team4u;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.team4u.id.domain.seq.SequenceConfig;
import org.team4u.id.domain.seq.value.InMemoryStepSequenceProvider;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceConfig;
import org.team4u.id.domain.seq.value.cache.CacheStepSequenceProvider;
import redis.clients.jedis.JedisPoolConfig;

public class TestUtil {

    public static StringRedisTemplate createRedisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();

        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(new JedisPoolConfig());
        factory.setHostName("10.199.244.210");
        factory.setPort(6379);
        factory.afterPropertiesSet();
        template.setConnectionFactory(factory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new JdkSerializationRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    public static CacheStepSequenceProvider cacheProvider(int step, int maxValue) {
        CacheStepSequenceConfig config = new CacheStepSequenceConfig();

        StepSequenceProvider.Config delegateConfig = new StepSequenceProvider.Config();
        delegateConfig.setStep(step);
        delegateConfig.setMaxValue((long) maxValue);
        InMemoryStepSequenceProvider sequenceProvider = new InMemoryStepSequenceProvider(delegateConfig);

        return new CacheStepSequenceProvider(config, sequenceProvider);
    }

    public static SequenceProvider.Context sequenceProviderContext() {
        SequenceConfig sequenceConfig = new SequenceConfig();
        sequenceConfig.setConfigId("TEST");
        return new SequenceProvider.Context(sequenceConfig, "TEST", null);
    }
}
