package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.db.Page;
import cn.hutool.db.handler.BeanHandler;
import cn.hutool.db.handler.BeanListHandler;
import cn.hutool.db.sql.Direction;
import cn.hutool.db.sql.Order;
import cn.hutool.db.sql.Query;
import org.team4u.base.error.NestedException;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * 基于hutool db的流程定义资源库
 *
 * @author jay.wu
 */
public class HutoolProcessDefinitionRepository extends AbstractProcessDefinitionRepository {

    private final Db db;

    public HutoolProcessDefinitionRepository(EventStore eventStore, DataSource dataSource) {
        super(eventStore);
        this.db = Db.use(dataSource);
    }

    @Override
    protected ProcessDefinitionDo processDefinitionDoOf(ProcessDefinitionId processDefinitionId) {
        try {
            if (processDefinitionId.hasVersion()) {
                return db.find(
                        Query.of(
                                Entity.create("process_definition")
                                        .set("process_definition_id", processDefinitionId.getId())
                                        .set("process_definition_version", processDefinitionId.getVersion())
                        ),
                        new BeanHandler<>(ProcessDefinitionDo.class)
                );
            } else {
                List<ProcessDefinitionDo> result = db.page(
                        Entity.create("process_definition")
                                .set("process_definition_id", processDefinitionId.getId()),
                        new Page(0, 1, new Order("id", Direction.DESC)),
                        new BeanListHandler<>(ProcessDefinitionDo.class)
                );
                return CollUtil.getFirst(result);
            }
        } catch (SQLException e) {
            throw new NestedException(e);
        }
    }

    @Override
    protected void insertProcessDefinition(ProcessDefinitionDo definitionDo) {
        try {
            Long id = db.insertForGeneratedKey(Entity.create("process_definition").parseBean(definitionDo, true, true));
            definitionDo.setId(id);
        } catch (SQLException e) {
            throw new NestedException(e);
        }
    }

    @Override
    protected void updateProcessDefinition(ProcessDefinitionDo definitionDo) {
        try {
            db.update(
                    Entity.create("process_definition").parseBean(definitionDo, true, true),
                    Entity.create().set("id", definitionDo.getId())
            );
        } catch (SQLException e) {
            throw new NestedException(e);
        }
    }
}
