package org.team4u.id.infrastructure.seq.mysql;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.team4u.id.domain.seq.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.SequenceProvider;
import org.team4u.id.infrastructure.seq.StepSequenceProvider;

import java.util.Date;

/**
 * 缓存号段序号值提供者
 *
 * @author jay.wu
 */
public class DbSequenceProvider implements StepSequenceProvider {

    private final Config config;
    private final SequenceMapper sequenceMapper;

    public DbSequenceProvider(Config config, SequenceMapper sequenceMapper) {
        this.config = config;
        this.sequenceMapper = sequenceMapper;
    }

    @Override
    public Number provide(Context context) {
        Sequence sequence = find(context.getSequenceConfig().getTypeId(), context.getGroupKey());

        // 序列不存在，进行插入
        if (sequence == null) {
            Number value = valueByCreate(context);
            if (value != null) {
                return value;
            }

            // 已存在记录，尝试更新
            sequence = find(context.getSequenceConfig().getTypeId(), context.getGroupKey());
        }

        return valueByUpdate(sequence, context);
    }

    private Number valueByUpdate(Sequence sequence, Context context) {
        // 超过最大值，且不循环使用，直接返回
        if (isOverMaxValue(sequence)) {
            if (!config().isRecycleAfterMaxValue()) {
                return sequence.getCurrentValue();
            }
        }

        // 序列存在，进行更新
        sequence.setUpdateTime(new Date());
        sequence.setCurrentValue(sequence.getCurrentValue() + config.getStep());

        // 如果超过最大值，将重置序列
        resetIfOverMaxValue(sequence);

        // 乐观锁尝试更新，失败将继续尝试直到成功
        return updateWithRetry(sequence, context);
    }

    private void resetIfOverMaxValue(Sequence sequence) {
        if (!isOverMaxValue(sequence)) {
            return;
        }

        // 循环使用
        if (config().isRecycleAfterMaxValue()) {
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

    private boolean isOverMaxValue(Sequence sequence) {
        return sequence.getCurrentValue() > config.getMaxValue();
    }

    private Number valueByCreate(Context context) {
        Sequence sequence = new Sequence(context.getSequenceConfig().getTypeId(), context.getGroupKey());
        sequence.setCurrentValue(config.getStart());
        sequence.setCreateTime(new Date());
        sequence.setUpdateTime(new Date());
        sequence.setVersionNumber(0L);

        try {
            sequenceMapper.insert(sequence);
            return sequence.getCurrentValue();
        } catch (DuplicateKeyException e) {
            // 已存在记录，尝试更新
            return null;
        }
    }

    /**
     * 查找序列
     */
    private Sequence find(String typeId, String groupKey) {
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
    public static class Factory extends AbstractSequenceProviderFactory {

        private final SequenceMapper sequenceMapper;

        @Autowired
        public Factory(SequenceMapper sequenceMapper) {
            this.sequenceMapper = sequenceMapper;
        }

        @Override
        public String id() {
            return "DB";
        }

        @Override
        protected SequenceProvider internalCreate(JSONObject config) {
            return new DbSequenceProvider(config.toBean(Config.class), sequenceMapper);
        }
    }
}