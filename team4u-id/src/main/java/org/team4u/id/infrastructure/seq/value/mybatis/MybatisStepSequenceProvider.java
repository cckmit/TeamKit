package org.team4u.id.infrastructure.seq.value.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.dao.DuplicateKeyException;
import org.team4u.base.bean.provider.BeanProviders;
import org.team4u.id.domain.seq.value.AbstractSequenceProviderFactory;
import org.team4u.id.domain.seq.value.SequenceProvider;
import org.team4u.id.infrastructure.seq.value.SqlStepSequenceProvider;
import org.team4u.id.infrastructure.seq.value.jdbc.Sequence;

/**
 * 基于mybatis的序号值提供者
 *
 * @author jay.wu
 */
public class MybatisStepSequenceProvider extends SqlStepSequenceProvider {

    private final SequenceMapper sequenceMapper;

    public MybatisStepSequenceProvider(Config config, SequenceMapper sequenceMapper) {
        super(config);
        this.sequenceMapper = sequenceMapper;
    }


    @Override
    protected int updateSequence(Sequence sequence) {
        return sequenceMapper.updateById(sequence);
    }

    @Override
    protected Long insertSequence(Sequence sequence) {
        try {
            sequenceMapper.insert(sequence);
            return sequence.getCurrentValue();
        } catch (DuplicateKeyException e) {
            return null;
        }
    }

    @Override
    protected Sequence sequenceOf(String typeId, String groupKey) {
        return sequenceMapper.selectOne(new LambdaQueryWrapper<Sequence>()
                .eq(Sequence::getTypeId, typeId)
                .eq(Sequence::getGroupKey, groupKey)
        );
    }

    public static class Factory extends AbstractSequenceProviderFactory<Config> {

        @Override
        public String id() {
            return "MBS";
        }

        @Override
        protected SequenceProvider createWithConfig(Config config) {
            return new MybatisStepSequenceProvider(
                    config,
                    BeanProviders.getInstance().getBean(SequenceMapper.class)
            );
        }
    }
}