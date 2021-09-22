package org.team4u.kv.infrastructure.repository.file;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.team4u.base.lang.aggregation.AbstractAggregationTask;
import org.team4u.base.lang.aggregation.AggregationTaskListener;
import org.team4u.base.lang.aggregation.TimedAggregationTask;
import org.team4u.kv.infrastructure.repository.memory.InMemoryKeyValueRepository;
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

    private final File db;
    private final Config config;
    private final TimedAggregationTask<Long> flushTask;

    public SimpleFileKeyValueRepository(Config config) {
        this.config = config;
        this.db = FileUtil.file(config.getDbPath());
        this.flushTask = initFlushTask();

        initCacheMap();

        // 尽量确保数据不丢失
        RuntimeUtil.addShutdownHook(this::flush);
    }

    private void initCacheMap() {
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

    private TimedAggregationTask<Long> initFlushTask() {
        return new TimedAggregationTask<>(
                config.getMaxBufferSize(), config.getFlushIntervalMillis(), new AggregationTaskListener<Long>() {
            @Override
            public void onReceive(AbstractAggregationTask<Long> task, Long value) {
            }

            @Override
            public void onFlush(AbstractAggregationTask<Long> task, List<Long> values) {
                flush();
            }
        });
    }

    private synchronized void flush() {
        List<String> lines = cache().values()
                .stream()
                .map(JSON::toJSONString)
                .collect(Collectors.toList());

        FileUtil.writeUtf8String(StrUtil.join("\n", lines), db);
    }

    private void save() {
        flushTask.add(System.currentTimeMillis());
    }

    @Override
    public KeyValueId save(KeyValue keyValue) {
        KeyValueId id = super.save(keyValue);
        save();
        return id;
    }

    @Override
    public KeyValueId saveIfAbsent(KeyValue keyValue) {
        KeyValueId id = super.saveIfAbsent(keyValue);
        save();
        return id;
    }

    @Override
    public void remove(KeyValueId id) {
        super.remove(id);
        save();
    }

    @Override
    public int removeExpirationValues(int maxBatchSize) {
        int count = super.removeExpirationValues(maxBatchSize);
        save();
        return count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        private String dbPath;
        @Builder.Default
        private int maxBufferSize = 10;
        @Builder.Default
        private long flushIntervalMillis = 1000;
    }
}