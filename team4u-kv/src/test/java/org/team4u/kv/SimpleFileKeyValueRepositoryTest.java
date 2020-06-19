package org.team4u.kv;

import cn.hutool.core.io.FileUtil;
import org.team4u.kv.infrastruture.repository.file.SimpleFileKeyValueRepository;

public class SimpleFileKeyValueRepositoryTest extends KeyValueServiceTest {

    @Override
    protected KeyValueRepository keyValueRepository() {
        return new SimpleFileKeyValueRepository(FileUtil.file("test_db.txt"));
    }
}
