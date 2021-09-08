package org.team4u.rl.application;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
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

}
