package org.team4u.workflow.infrastructure.definition;

import cn.hutool.core.util.StrUtil;
import org.team4u.base.config.ConfigService;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.definition.exception.ProcessDefinitionNotExistException;

/**
 * 基于Json的流程定义资源库
 *
 * @author jay.wu
 */
public class JsonProcessDefinitionRepository implements ProcessDefinitionRepository {

    private final ConfigService configService;

    public JsonProcessDefinitionRepository(ConfigService configService) {
        this.configService = configService;
    }

    @Override
    public ProcessDefinition domainOf(String domainId) {
        ProcessDefinitionId processDefinitionId = ProcessDefinitionId.of(domainId);

        String json;
        if (processDefinitionId.hasVersion()) {
            json = configService.get(domainId);
        } else {
            json = configService.get(processDefinitionId.getId());
        }

        if (StrUtil.isBlank(json)) {
            throw new ProcessDefinitionNotExistException(domainId);
        }

        return ProcessDefinitionUtil.definitionOfJson(processDefinitionId, json);
    }

    @Override
    public void save(ProcessDefinition domain) {
        throw new UnsupportedOperationException("save");
    }
}