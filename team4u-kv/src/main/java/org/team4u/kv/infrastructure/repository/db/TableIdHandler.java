package org.team4u.kv.infrastructure.repository.db;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;

public interface TableIdHandler extends TableNameHandler {

    String tableName();
}
