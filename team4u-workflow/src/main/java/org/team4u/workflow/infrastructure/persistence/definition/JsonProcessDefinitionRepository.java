package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.core.io.FileUtil;
import cn.hutool.log.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.team4u.base.config.ConfigService;
import org.team4u.base.log.LogMessage;
import org.team4u.base.util.PathUtil;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;

import java.io.File;

/**
 * 基于Json的流程定义资源库
 *
 * @author jay.wu
 */
public class JsonProcessDefinitionRepository implements ProcessDefinitionRepository {

    private final Log log = Log.get();

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

        return ProcessDefinitionUtil.definitionOfJson(processDefinitionId, json);
    }

    @Override
    public void save(ProcessDefinition domain) {
        File file = FileUtil.file(PathUtil.runningPath(domain.getProcessDefinitionId().getId() + ".json"));

        FileUtil.writeUtf8String(
                JSON.toJSONString(
                        domain,
                        SerializerFeature.WriteClassName,
                        SerializerFeature.PrettyFormat
                ),
                file
        );

        log.info(LogMessage.create(this.getClass().getSimpleName(), "save")
                .success()
                .append("path", file.getAbsolutePath())
                .toString());
    }
}