package org.team4u.id.infrastructure.seq.value.jdbc;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.BeanHandler;
import cn.hutool.db.sql.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.base.error.NestedException;
import org.team4u.id.domain.seq.value.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.domain.seq.value.StepSequenceProvider;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 基于JDBC的序号提供者
 *
 * @author jay.wu
 */
public class JdbcStepSequenceProvider extends RdbmsStepSequenceProvider {

    private final Db db;

    private final Config config;

    public JdbcStepSequenceProvider(Config config, DataSource dataSource) {
        this.config = config;
        this.db = Db.use(dataSource);
    }

    @Override
    protected int updateSequence(Sequence sequence) {
        long version = sequence.getVersionNumber();
        sequence.setVersionNumber(version + 1);

        try {
            return db.update(
                    Entity.create().parseBean(sequence, true, true),
                    Entity.create()
                            .set("id", sequence.getId())
                            .set("version_number", version)
            );
        } catch (SQLException e) {
            throw new NestedException(e);
        }
    }

    @Override
    protected Long insertSequence(Sequence sequence) {
        try {
            db.insert(Entity.create().parseBean(sequence, true, true));
            return sequence.getCurrentValue();
        } catch (SQLIntegrityConstraintViolationException e) {
            // 已存在记录，尝试更新
            return null;
        } catch (SQLException e) {
            throw new NestedException(e);
        }
    }

    @Override
    protected Sequence sequenceOf(String configId, String groupKey) {
        try {
            return db.find(
                    Query.of(
                            Entity.create("sequence")
                                    .set("config_id", configId)
                                    .set("group_key", groupKey)
                    ),
                    new BeanHandler<>(Sequence.class)
            );
        } catch (SQLException e) {
            throw new NestedException(e);
        }
    }

    @Override
    public Config config() {
        return config;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Config extends StepSequenceProvider.Config {
        /**
         * 数据源 bean标识，若不指定则默认获取类型为DataSource的bean
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
                    BeanProviders.getBean(DataSource.class, config.getDataSourceBeanId())
            );
        }
    }
}