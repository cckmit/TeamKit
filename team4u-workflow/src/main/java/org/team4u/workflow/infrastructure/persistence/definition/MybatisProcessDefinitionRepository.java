package org.team4u.workflow.infrastructure.persistence.definition;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;

/**
 * 基于Mybatis的流程定义资源库
 *
 * @author jay.wu
 */
public class MybatisProcessDefinitionRepository extends AbstractProcessDefinitionRepository {

    private final ProcessDefinitionMapper definitionMapper;

    public MybatisProcessDefinitionRepository(EventStore eventStore,
                                              ProcessDefinitionMapper definitionMapper) {
        super(eventStore);
        this.definitionMapper = definitionMapper;
    }

    @Override
    protected ProcessDefinitionDo processDefinitionDoOf(ProcessDefinitionId processDefinitionId) {
        ProcessDefinitionDo definitionDo;
        if (processDefinitionId.hasVersion()) {
            definitionDo = definitionMapper.selectOne(
                    new LambdaQueryWrapper<ProcessDefinitionDo>()
                            .eq(ProcessDefinitionDo::getProcessDefinitionId, processDefinitionId.getId())
                            .eq(ProcessDefinitionDo::getProcessDefinitionVersion, processDefinitionId.getVersion())
            );
        } else {
            definitionDo = definitionMapper.selectOne(
                    new LambdaQueryWrapper<ProcessDefinitionDo>()
                            .eq(ProcessDefinitionDo::getProcessDefinitionId, processDefinitionId.getId())
                            .orderByDesc(ProcessDefinitionDo::getId)
            );
        }

        return definitionDo;
    }

    @Override
    protected void insertProcessDefinition(ProcessDefinitionDo definitionDo) {
        definitionMapper.insert(definitionDo);
    }

    @Override
    protected void updateProcessDefinition(ProcessDefinitionDo definitionDo) {
        definitionMapper.updateById(definitionDo);
    }
}