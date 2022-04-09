package org.team4u.kv.infrastructure.repository.db;

import cn.hutool.core.lang.func.Func0;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.team4u.base.log.LogMessage;
import org.team4u.kv.KeyValueRepository;
import org.team4u.kv.model.KeyValue;
import org.team4u.kv.model.KeyValueFactory;
import org.team4u.kv.model.KeyValueId;
import org.team4u.kv.resource.StoreResource;
import org.team4u.kv.resource.StoreResourceService;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于数据库的kv资源库
 *
 * @author jay.wu
 */
public class DbKeyValueRepository implements KeyValueRepository {

    private final Log log = LogFactory.get();

    private final DbKeyValueMapper dbKeyValueMapper;
    private final StoreResourceService storeResourceService;

    public DbKeyValueRepository(DbKeyValueMapper dbKeyValueMapper,
                                StoreResourceService storeResourceService) {
        this.dbKeyValueMapper = dbKeyValueMapper;
        this.storeResourceService = storeResourceService;
    }

    @Override
    public KeyValue byId(KeyValueId id) {
        withResource(id);

        KeyValueEntity keyValueEntity = dbKeyValueMapper.selectOne(
                new LambdaQueryWrapper<KeyValueEntity>()
                        .eq(KeyValueEntity::getType, id.getType())
                        .eq(KeyValueEntity::getName, id.getName())
        );

        return toKeyValue(keyValueEntity);
    }

    @Override
    public List<KeyValue> byType(String type) {
        return withResources(() -> dbKeyValueMapper.selectList(
                new LambdaQueryWrapper<KeyValueEntity>()
                        .eq(KeyValueEntity::getType, type)
                        .and(this::notExpirationWrapper)))
                .stream()
                .flatMap(Collection::stream)
                .map(this::toKeyValue)
                .collect(Collectors.toList());
    }

    @Override
    public int removeExpirationValues(int maxBatchSize) {
        return withResources(() -> {
            List<Long> idListToRemove = dbKeyValueMapper.selectPage(
                            new Page<KeyValueEntity>(1, maxBatchSize).setSearchCount(false),
                            new LambdaQueryWrapper<KeyValueEntity>()
                                    .select(KeyValueEntity::getId)
                                    .and(this::expirationWrapper)
                    )
                    .getRecords()
                    .stream()
                    .map(KeyValueEntity::getId)
                    .collect(Collectors.toList());

            if (idListToRemove.isEmpty()) {
                return null;
            }

            int count = dbKeyValueMapper.delete(
                    new LambdaQueryWrapper<KeyValueEntity>()
                            .in(KeyValueEntity::getId, idListToRemove)
            );

            log.info(LogMessage.create(this.getClass().getSimpleName(), "removeExpirationValues")
                    .success()
                    .append("table", TableNameContext.getTableName())
                    .append("count", count)
                    .toString());

            return count;
        })
                .stream()
                .reduce(0, Integer::sum);
    }

    @Override
    public KeyValueId save(KeyValue keyValue) {
        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "save")
                .append("id", keyValue.id());

        KeyValueEntity entity = toKeyValueEntity(keyValue).setUpdateTime(new Date());

        // 尝试插入
        if (!containsKey(keyValue.id())) {
            KeyValueId id = saveIfAbsent(keyValue);
            if (id != null) {
                return id;
            }
        }

        // 尝试更新
        if (!update(entity)) {
            log.error(lm.fail().toString());
            return null;
        }

        log.info(lm.success().toString());

        return keyValue.id();
    }

    @Override
    public KeyValueId saveIfAbsent(KeyValue keyValue) {
        if (keyValue == null) {
            return null;
        }

        StoreResource resource = withResource(keyValue.id());

        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "saveIfAbsent")
                .append("resource", resource.toString())
                .append("id", keyValue.id());


        KeyValueEntity entity = toKeyValueEntity(keyValue);
        // 先删除过期记录
        boolean isRemoveIfExpired = removeIfExpired(entity);
        lm.append("isRemoveIfExpired", isRemoveIfExpired);

        // 再尝试插入
        if (insert(entity)) {
            log.info(lm.success().toString());
            return keyValue.id();
        }

        log.info(lm.fail("duplicateKey").toString());
        return null;
    }

    private boolean update(KeyValueEntity entity) {
        int count = dbKeyValueMapper.update(
                entity.setUpdateTime(new Date()),
                new LambdaQueryWrapper<KeyValueEntity>()
                        .eq(KeyValueEntity::getType, entity.getType())
                        .eq(KeyValueEntity::getName, entity.getName())
        );

        return count > 0;
    }

    private boolean insert(KeyValueEntity entity) {
        try {
            // 尝试插入
            dbKeyValueMapper.insert(
                    entity.setCreateTime(new Date()).setUpdateTime(new Date())
            );
            return true;
        } catch (DuplicateKeyException e) {
            return false;
        }
    }

    private boolean removeIfExpired(KeyValueEntity entity) {
        int count = dbKeyValueMapper.delete(
                new LambdaQueryWrapper<KeyValueEntity>()
                        .eq(KeyValueEntity::getType, entity.getType())
                        .eq(KeyValueEntity::getName, entity.getName())
                        .and(this::expirationWrapper)
        );

        return count > 0;
    }

    @Override
    public void clear(String type) {
        withResources(() -> {
            int count = dbKeyValueMapper.delete(
                    new LambdaQueryWrapper<KeyValueEntity>()
                            .eq(KeyValueEntity::getType, type)
            );

            log.info(LogMessage.create(this.getClass().getSimpleName(), "clear")
                    .success()
                    .append("count", count)
                    .toString());

            return null;
        });
    }

    @Override
    public void remove(KeyValueId id) {
        StoreResource resource = withResource(id);

        int count = dbKeyValueMapper.delete(
                new LambdaQueryWrapper<KeyValueEntity>()
                        .eq(KeyValueEntity::getType, id.getType())
                        .eq(KeyValueEntity::getName, id.getName())
        );

        log.debug(LogMessage.create(this.getClass().getSimpleName(), "remove")
                .success()
                .append("resource", resource.toString())
                .append("id", id.toString())
                .append("count", count)
                .toString());
    }

    @Override
    public long count(String type) {
        return withResources(() -> dbKeyValueMapper.selectCount(
                new LambdaQueryWrapper<KeyValueEntity>()
                        .eq(KeyValueEntity::getType, type)
                        .and(this::notExpirationWrapper)))
                .stream()
                .reduce(0L, Long::sum);
    }

    @Override
    public boolean containsKey(KeyValueId id) {
        withResource(id);

        return dbKeyValueMapper.selectCount(
                new LambdaQueryWrapper<KeyValueEntity>()
                        .eq(KeyValueEntity::getType, id.getType())
                        .eq(KeyValueEntity::getName, id.getName())
                        .and(this::notExpirationWrapper)
        ) > 0;
    }

    private KeyValue toKeyValue(KeyValueEntity entity) {
        if (entity == null) {
            return null;
        }

        KeyValue sv = KeyValueFactory.create(
                entity.getType(),
                entity.getName(),
                entity.getValue(),
                entity.getExpirationTimestamp()
        );

        if (sv.isExpired()) {
            return null;
        }

        return sv;
    }

    private KeyValueEntity toKeyValueEntity(KeyValue kv) {
        return new KeyValueEntity()
                .setType(kv.id().getType())
                .setName(kv.id().getName())
                .setValue(kv.getValue())
                .setExpirationTimestamp(kv.getExpirationTimestamp());
    }

    /**
     * 构建不过期的查询条件
     */
    private void notExpirationWrapper(LambdaQueryWrapper<KeyValueEntity> wrapper) {
        wrapper.ge(KeyValueEntity::getExpirationTimestamp, new Date().getTime())
                .or()
                .eq(KeyValueEntity::getExpirationTimestamp, 0);
    }

    /**
     * 构建过期的查询条件
     */
    private void expirationWrapper(LambdaQueryWrapper<KeyValueEntity> wrapper) {
        wrapper.lt(KeyValueEntity::getExpirationTimestamp, new Date().getTime())
                .gt(KeyValueEntity::getExpirationTimestamp, 0);
    }

    /**
     * 根据键值标识分配具体的资源（表名）
     */
    private StoreResource withResource(KeyValueId id) {
        StoreResource resource = storeResourceService.select(id.toString());
        TableNameContext.setTableName(resource.getName(), resource.getId());
        return resource;
    }

    /**
     * 遍历所有资源进行操作，并聚合最终结果
     */
    private <V> List<V> withResources(Func0<V> runnable) {
        return storeResourceService.resources()
                .stream()
                .map(resource -> {
                    TableNameContext.setTableName(resource.getName(), resource.getId());
                    try {
                        return runnable.call();
                    } catch (Exception e) {
                        log.error(e, LogMessage.create(this.getClass().getSimpleName(), "withResources")
                                .append("resource", resource)
                                .fail(e.getMessage())
                                .toString());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}