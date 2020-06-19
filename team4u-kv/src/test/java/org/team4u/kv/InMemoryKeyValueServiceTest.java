package org.team4u.kv;

import org.team4u.kv.infrastruture.repository.memory.InMemoryKeyValueRepository;

public class InMemoryKeyValueServiceTest extends KeyValueServiceTest {

    @Override
    protected KeyValueRepository keyValueRepository() {
        return new InMemoryKeyValueRepository();
    }
}