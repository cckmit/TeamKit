package org.team4u.workflow.infrastructure.persistence;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.team4u.base.config.ConfigService;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;

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
        if (processDefinitionId.getVersion() < 0) {
            json = configService.get(processDefinitionId.getId());
        } else {
            json = configService.get(domainId);
        }

        if (StrUtil.isBlank(json)) {
            throw new SystemDataNotExistException("ProcessDefinition json is null|id=" + domainId);
        }

        return JSON.parseObject(json, ProcessDefinition.class, Feature.SupportAutoType);
    }

    @Override
    public void save(ProcessDefinition domain) {
        throw new UnsupportedOperationException("save");
    }
}