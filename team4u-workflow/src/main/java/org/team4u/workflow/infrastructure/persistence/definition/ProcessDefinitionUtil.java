package org.team4u.workflow.infrastructure.persistence.definition;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;

/**
 * 流程定义工具类
 *
 * @author jay.wu
 */
public class ProcessDefinitionUtil {

    /**
     * 根据Json生成流程定义
     *
     * @param processDefinitionId 流程定义标识
     * @param json                流程定义Json
     * @return 流程定义
     */
    public static ProcessDefinition definitionOfJson(ProcessDefinitionId processDefinitionId,
                                                     String json) {
        if (StrUtil.isBlank(json)) {
            return null;
        }

        ProcessDefinition definition = JSON.parseObject(
                json, ProcessDefinition.class, Feature.SupportAutoType
        );

        ReflectUtil.setFieldValue(definition, "processDefinitionId", processDefinitionId);
        return definition;
    }
}