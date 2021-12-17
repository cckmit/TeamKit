package org.team4u.id.infrastructure.seq.value.jdbc;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.BeanHandler;
import cn.hutool.db.sql.Query;
import cn.hutool.log.Log;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.error.NestedException;
import org.team4u.base.log.LogMessage;
import org.team4u.id.domain.seq.value.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;

/**
 * 基于mybatis的序号值提供者
 *
 * @author jay.wu
 */
public class JdbcStepSequenceProvider implements StepSequenceProvider {

    private final Log log = Log.get();

    private final Config config;
    private final Db db;

    public JdbcStepSequenceProvider(Config config, DataSource dataSource) {
        this.config = config;
        this.db = Db.use(dataSource);
    }

    @Override
    public Number provide(Context context) {
        try {
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
        } catch (Exception e) {
            throw new NestedException(e);
        }
    }

    /**
     * 当前序号值
     *
     * @param context 上下文
     */
    public Number currentSequence(Context context) throws SQLException {
        Sequence sequence = sequenceOf(context.getSequenceConfig().getTypeId(), context.getGroupKey());

        if (sequence == null) {
            return null;
        }

        return sequence.getCurrentValue();
    }

    private Number sequenceValueByUpdate(Sequence sequence, Context context) throws SQLException {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "sequenceValueByUpdate")
                .append("context", context);

        // 若当前值超过最大值，且不循环使用，直接返回
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

    private Number updateWithRetry(Sequence sequence, Context context) throws SQLException {
        long version = sequence.getVersionNumber();
        sequence.setVersionNumber(version + 1);
        int result = db.update(
                Entity.create().parseBean(sequence, true, true),
                Entity.create()
                        .set("id", sequence.getId())
                        .set("version_number", version)
        );

        // 若更新失败，重新尝试
        if (result == 0) {
            long sleepMills = RandomUtil.randomLong(1, 100);
            log.info(LogMessage.create(this.getClass().getSimpleName(), "updateWithRetry")
                    .append("context", context)
                    .append("sleepMills", sleepMills)
                    .fail("Retrying")
                    .toString());
            // 防止同时请求，增加随即延时
            ThreadUtil.sleep(sleepMills);
            return provide(context);
        }

        return sequence.getCurrentValue();
    }

    private Number sequenceValueByCreate(Context context) throws SQLException {
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
            db.insert(Entity.create().parseBean(sequence, true, true));
            log.info(lm.success().append("currentValue", sequence.getCurrentValue()).toString());
            return sequence.getCurrentValue();
        } catch (SQLIntegrityConstraintViolationException e) {
            // 已存在记录，尝试更新
            log.info(lm.fail("tryUpdate").toString());
            return null;
        }
    }

    /**
     * 查找序列
     */
    private Sequence sequenceOf(String typeId, String groupKey) throws SQLException {
        return db.find(
                Query.of(
                        Entity.create("sequence")
                                .set("type_id", typeId)
                                .set("group_key", groupKey)
                ),
                new BeanHandler<>(Sequence.class)
        );
    }

    @Override
    public Config config() {
        return config;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Config extends StepSequenceProvider.Config {
        /**
         * 数据源 bean标识
         */
        private String dataSourceBeanId;
    }

    public static class Factory extends AbstractSequenceProviderFactory<Config> {

        @Override
        public String id() {
            return "JS";
        }

        @Override
        protected SequenceProvider createWithConfig(Config config) {
            return new JdbcStepSequenceProvider(
                    config,
                    BeanProviders.getInstance().getBean(config.getDataSourceBeanId())
            );
        }
    }
}