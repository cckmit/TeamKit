package org.team4u.ddd.infrastructure.persistence.mybatis;

import org.team4u.ddd.idempotent.IdempotentValue;
import org.team4u.ddd.idempotent.IdempotentValueStore;
import org.springframework.dao.DuplicateKeyException;
import org.team4u.base.error.IdempotentException;

import java.util.Date;

public class MybatisIdempotentValueStore implements IdempotentValueStore {

    private final IdempotentValueMapper mapper;

    public MybatisIdempotentValueStore(IdempotentValueMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void append(IdempotentValue value) throws IdempotentException {
        try {
            IdempotentValueEntity entity = new IdempotentValueEntity()
                    .setIdempotentId(value.getIdempotentId())
                    .setTypeName(value.getTypeName())
                    .setCreateTime(new Date());
            mapper.insert(entity);
        } catch (DuplicateKeyException e) {
            throw new IdempotentException();
        }
    }
}