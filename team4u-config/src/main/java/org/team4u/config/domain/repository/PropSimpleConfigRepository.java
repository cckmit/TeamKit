package org.team4u.config.domain.repository;

import cn.hutool.setting.dialect.Props;

public class PropSimpleConfigRepository extends MapSimpleConfigRepository {

    public PropSimpleConfigRepository(String path) {
        super(new Props(path));
    }
}