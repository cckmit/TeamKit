package org.team4u.id.infrastructure.seq.value.mybatis;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.team4u.base.log.LogMessage;
import org.team4u.id.domain.seq.value.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import java.util.Date;

/**
 * 基于mybatis的序号值提供者
 *
 * @author jay.wu
 */
public class MybatisStepSequenceProvider implements StepSequenceProvider {

    private final Log log = Log.get();

    private final Config config;
    private final SequenceMapper sequenceMapper;

    public MybatisStepSequenceProvider(Config config, SequenceMapper sequenceMapper) {
        this.config = config;
        this.sequenceMapper = sequenceMapper;
    }

    @Override
    public Number provide(Context context) {
        Sequence sequence = sequenceOf(context.getSequenceConfig().getTypeId(), context.getGroupKey());

        // 序列不存在，进行插入
        if (sequence == null) {
            Number value = sequenceValueByCreate(context);
            if (value != null) {
                return value;
            }

            // 已存在记录，尝试更新
            sequence = sequenceOf(context.getSequenceConfig().getTypeId(), context.getGroupKey());
        }

        return sequenceValueByUpdate(sequence, context);
    }

    private Number sequenceValueByUpdate(Sequence sequence, Context context) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "sequenceValueByUpdate")
                .append("context", context);

        // 超过最大值，且不循环使用，直接返回
        if (!canUpdateIfOverMaxValue(sequence)) {
            log.info(lm.fail().append("canUpdateIfOverMaxValue", false).toString());
            return null;
        }

        // 序列存在，进行更新
        sequence.setUpdateTime(new Date());
        sequence.setCurrentValue(sequence.getCurrentValue() + config.getStep());

        // 如果超过最大值，将重置序列
        resetIfOverMaxValue(sequence);

        // 乐观锁尝试更新，失败将继续尝试直到成功
        Number result = updateWithRetry(sequence, context);
        log.info(lm.success().append("currentValue", result).toString());
        return result;
    }

    private boolean canUpdateIfOverMaxValue(Sequence sequence) {
        if (sequence.getCurrentValue() < config.getMaxValue()) {
            return true;
        }

        return config.isRecycleAfterMaxValue();
    }

    private void resetIfOverMaxValue(Sequence sequence) {
        if (sequence.getCurrentValue() <= config.getMaxValue()) {
            return;
        }

        // 循环使用
        if (config.isRecycleAfterMaxValue()) {
            sequence.setCurrentValue(config.getStart());
        } else {
            sequence.setCurrentValue(config.getMaxValue());
        }
    }

    private Number updateWithRetry(Sequence sequence, Context context) {
        int result = sequenceMapper.updateById(sequence);

        // 若更新失败，重新尝试
        if (result == 0) {
            // 防止同时请求，增加随即延时
            ThreadUtil.sleep(RandomUtil.randomLong(1, 100));
            return provide(context);
        }

        return sequence.getCurrentValue();
    }

    private Number sequenceValueByCreate(Context context) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "sequenceValueByCreate")
                .append("context", context);
        Sequence sequence = new Sequence();
        sequence.setTypeId(context.getSequenceConfig().getTypeId());
        sequence.setGroupKey(context.getGroupKey());
        sequence.setCurrentValue(config.getStart());
        sequence.setCreateTime(new Date());
        sequence.setUpdateTime(new Date());
        sequence.setVersionNumber(0L);

        try {
            sequenceMapper.insert(sequence);
            log.info(lm.success().append("currentValue", sequence.getCurrentValue()).toString());
            return sequence.getCurrentValue();
        } catch (DuplicateKeyException e) {
            // 已存在记录，尝试更新
            log.info(lm.fail("tryUpdate").toString());
            return null;
        }
    }

    /**
     * 查找序列
     */
    private Sequence sequenceOf(String typeId, String groupKey) {
        return sequenceMapper.selectOne(new LambdaQueryWrapper<Sequence>()
                .eq(Sequence::getTypeId, typeId)
                .eq(Sequence::getGroupKey, groupKey)
        );
    }

    @Override
    public Config config() {
        return config;
    }

    @Component
    public static class Factory extends AbstractSequenceProviderFactory<Config> {

        private final SequenceMapper sequenceMapper;

        @Autowired
        public Factory(SequenceMapper sequenceMapper) {
            this.sequenceMapper = sequenceMapper;
        }

        @Override
        public String id() {
            return "MBS";
        }

        @Override
        protected SequenceProvider createWithConfig(Config config) {
            return new MybatisStepSequenceProvider(config, sequenceMapper);
        }
    }
}