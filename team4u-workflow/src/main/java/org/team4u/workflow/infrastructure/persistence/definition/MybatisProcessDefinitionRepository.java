package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.ddd.domain.model.DomainEventAwareRepository;
import org.team4u.ddd.event.EventStore;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;

import java.util.Date;

/**
 * 基于数据库的流程定义资源库
 *
 * @author jay.wu
 */
public class MybatisProcessDefinitionRepository
        extends DomainEventAwareRepository<ProcessDefinition>
        implements ProcessDefinitionRepository {

    private final ProcessDefinitionMapper definitionMapper;

    public MybatisProcessDefinitionRepository(EventStore eventStore,
                                              ProcessDefinitionMapper definitionMapper) {
        super(eventStore);
        this.definitionMapper = definitionMapper;
    }

    @Override
    public ProcessDefinition domainOf(String domainId) {
        ProcessDefinitionDo definitionDo = processDefinitionDoOf(ProcessDefinitionId.of(domainId));
        return toProcessDefinition(definitionDo);
    }

    private ProcessDefinitionDo processDefinitionDoOf(ProcessDefinitionId processDefinitionId) {
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

    private ProcessDefinition toProcessDefinition(ProcessDefinitionDo definitionDo) {
        String json = definitionDo.getProcessDefinitionBody();

        if (StrUtil.isBlank(json)) {
            throw new SystemDataNotExistException("ProcessDefinition json is null|id=" + definitionDo.getId());
        }

        return JSON.parseObject(json, ProcessDefinition.class, Feature.SupportAutoType);
    }

    @Override
    protected void doSave(ProcessDefinition definition) {
        ProcessDefinitionDo definitionDo = toProcessDefinitionDo(definition);
        if (definition.getId() == null) {
            definitionDo.setCreateTime(new Date());
            definitionMapper.insert(definitionDo);
            definition.setId(definitionDo.getId());
        } else {
            definitionDo.setUpdateTime(new Date());
            definitionMapper.updateById(definitionDo);
        }
    }

    private ProcessDefinitionDo toProcessDefinitionDo(ProcessDefinition definition) {
        ProcessDefinitionDo definitionDo = new ProcessDefinitionDo();
        definitionDo.setId(definitionDo.getId());
        definitionDo.setProcessDefinitionId(definition.getProcessDefinitionId().getId());
        definitionDo.setProcessDefinitionVersion(definition.getProcessDefinitionId().getVersion());

        definitionDo.setProcessDefinitionBody(
                JSON.toJSONString(
                        definition,
                        SerializerFeature.WriteClassName
                )
        );

        return definitionDo;
    }
}