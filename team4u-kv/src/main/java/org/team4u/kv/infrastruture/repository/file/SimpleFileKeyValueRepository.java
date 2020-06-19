package org.team4u.kv.infrastruture.repository.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.team4u.kv.infrastruture.repository.memory.InMemoryKeyValueRepository;
import org.team4u.kv.model.KeyValue;
import org.team4u.kv.model.KeyValueId;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于文件的简单kv存储
 * <p>
 * 仅适用于量小，性能要求不高的场景
 */
public class SimpleFileKeyValueRepository extends InMemoryKeyValueRepository {

    private File db;

    public SimpleFileKeyValueRepository(File db) {
        this.db = db;

        initCacheMap();
    }

    public void initCacheMap() {
        if (!FileUtil.exist(db)) {
            return;
        }

        for (String line : FileUtil.readUtf8Lines(db)) {
            if (StrUtil.isEmpty(line)) {
                continue;
            }

            KeyValue kv = JSON.parseObject(line, KeyValue.class);
            if (kv == null || kv.id() == null) {
                continue;
            }

            super.save(kv);
        }
    }

    public void flush() {
        List<String> lines = cache().values()
                .stream()
                .map(JSON::toJSONString)
                .collect(Collectors.toList());

        FileUtil.writeUtf8String(StrUtil.join("\n", lines), db);
    }

    @Override
    public KeyValueId save(KeyValue keyValue) {
        KeyValueId id = super.save(keyValue);
        flush();
        return id;
    }

    @Override
    public KeyValueId saveIfAbsent(KeyValue keyValue) {
        KeyValueId id = super.saveIfAbsent(keyValue);
        flush();
        return id;
    }

    @Override
    public void remove(KeyValueId id) {
        super.remove(id);
        flush();
    }

    @Override
    public int removeExpirationValues(int maxBatchSize) {
        int count = super.removeExpirationValues(maxBatchSize);
        flush();
        return count;
    }
}