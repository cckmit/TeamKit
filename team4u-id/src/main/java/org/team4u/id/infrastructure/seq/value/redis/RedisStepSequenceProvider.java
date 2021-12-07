package org.team4u.id.infrastructure.seq.value.redis;

import cn.hutool.core.convert.Convert;
import cn.hutool.log.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.log.LogMessage;
import org.team4u.id.domain.seq.value.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import java.util.Objects;

/**
 * 基于redis的序号值提供者
 *
 * @author jay.wu
 */
public class RedisStepSequenceProvider implements StepSequenceProvider {

    private final Log log = Log.get();

    private final Config config;
    private final RedisTemplate<String, String> redisTemplate;

    public RedisStepSequenceProvider(Config config, RedisTemplate<?, ?> redisTemplate) {
        this.config = config;
        this.redisTemplate = standardizedRedisTemplate(redisTemplate);
    }

    @Override
    public Number provide(Context context) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "provide")
                .append("context", context);

        String sequenceId = sequenceIdOf(context);

        // 若当前值超过最大值，且不循环使用，直接返回
        if (!canUpdateIfOverMaxValue(currentSequence(sequenceId))) {
            log.info(lm.fail().append("canUpdateIfOverMaxValue", false).toString());
            return null;
        }

        // 如果超过最大值，将重置序列
        Long newValue = resetIfOverMaxValue(updateSequence(sequenceId));
        log.info(lm.append("currentValue", newValue).success().toString());

        return newValue;
    }

    /**
     * 当前序号值
     *
     * @param context 上下文
     */
    public Number currentSequence(Context context) {
        return currentSequence(sequenceIdOf(context));
    }

    /**
     * 获取redis中的序号标识
     */
    protected String sequenceIdOf(Context context) {
        return "RSS:" + context.getSequenceConfig().getTypeId() + ":" + context.getGroupKey();
    }

    private Long updateSequence(String key) {
        return standardizedValue(redisTemplate.opsForValue().increment(key, config.getStep()));
    }

    private boolean canUpdateIfOverMaxValue(Long value) {
        if (value == null) {
            return true;
        }

        if (value < config.getMaxValue()) {
            return true;
        }

        return config.isRecycleAfterMaxValue();
    }

    /**
     * 修正redis存储的值
     */
    private Long standardizedValue(Long value) {
        if (value == null) {
            return null;
        }

        // redis的increment返回的是从0开始累加后的值，而我们期望返回累加前值，并且从start开始
        return value - config.getStep() + config.getStart();
    }

    private Long resetIfOverMaxValue(Long value) {
        if (value <= config.getMaxValue()) {
            return value;
        }

        // 循环使用
        if (config.isRecycleAfterMaxValue()) {
            return config.valueWithRecycle(value).longValue();
        }

        return config.getMaxValue();
    }

    private Long currentSequence(String key) {
        return standardizedValue(Convert.toLong(redisTemplate.opsForValue().get(key)));
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
    }
}