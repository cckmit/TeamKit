package org.team4u.kv;

import org.team4u.kv.infrastruture.repository.memory.InMemoryKeyValueRepository;

public class InMemoryKeyValueCleanerTest extends KeyValueCleanerTest {

    @Override
    protected KeyValueRepository keyValueRepository() {
        return new InMemoryKeyValueRepository();
    }
}