package org.team4u.id.infrastructure.seq.value.redis;

import cn.hutool.core.convert.Convert;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.value.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.value.AutoIncrementStepSequenceProvider;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import java.util.Objects;

/**
 * 基于redis的序号值提供者
 *
 * @author jay.wu
 */
public class RedisStepSequenceProvider extends AutoIncrementStepSequenceProvider {

    private final Config config;
    private final RedisTemplate<String, String> redisTemplate;

    public RedisStepSequenceProvider(Config config, RedisTemplate<?, ?> redisTemplate) {
        this.config = config;
        this.redisTemplate = standardizedRedisTemplate(redisTemplate);
    }

    @Override
    public Number currentSequence(Context context) {
        return Convert.toLong(redisTemplate.opsForValue().get(sequenceIdOf(context)));
    }

    @Override
    protected Number addAndGet(Context context) {
        return redisTemplate.opsForValue().increment(sequenceIdOf(context), config.getStep());
    }

    /**
     * 获取redis中的序号标识
     * <p>
     * 标准格式为：前缀:类型:分组
     */
    protected String sequenceIdOf(Context context) {
        return config.getRedisKeyPrefix() + ":" +
                context.getSequenceConfig().getTypeId() + ":" +
                context.getGroupKey();
    }

    @Override
    public Config config() {
        return config;
    }

    /**
     * 标准化redis template，防止序列化器与预期不符
     */
    private RedisTemplate<String, String> standardizedRedisTemplate(RedisTemplate<?, ?> redisTemplate) {
        // 需要确保key和value为string序列化模式，否则反序列化时可能异常
        if (redisTemplate.getKeySerializer() instanceof StringRedisSerializer &&
                redisTemplate.getValueSerializer() instanceof StringRedisSerializer) {
            //noinspection unchecked
            return (RedisTemplate<String, String>) redisTemplate;
        }

        return new StringRedisTemplate(
                Objects.requireNonNull(redisTemplate.getConnectionFactory())
        );
    }

    public static class Factory extends AbstractSequenceProviderFactory<Config> {

        @Override
        public String id() {
            return "RSS";
        }

        @Override
        protected SequenceProvider createWithConfig(Config config) {
            return new RedisStepSequenceProvider(
                    config,
                    BeanProviders.getInstance().getBean(config.getRedisTemplateBeanId())
            );
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Config extends StepSequenceProvider.Config {
        /**
         * redis template bean标识
         * <p>
         * 需要满足RedisTemplate<String, ?>
         */
        private String redisTemplateBeanId;
        /**
         * Redis的键前缀
         */
        private String redisKeyPrefix = "RSS";
    }
}