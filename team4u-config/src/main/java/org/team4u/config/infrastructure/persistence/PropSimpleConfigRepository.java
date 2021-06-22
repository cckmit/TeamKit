package org.team4u.config.infrastructure.persistence;

import cn.hutool.setting.dialect.Props;

public class PropSimpleConfigRepository extends MapSimpleConfigRepository {

    public PropSimpleConfigRepository(String path) {
        super(new Props(path));
    }
}