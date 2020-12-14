package org.team4u.workflow.domain.instance;

import cn.hutool.core.util.IdUtil;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessNode;

/**
 * 流程实例服务
 *
 * @author jay.wu
 */
public class ProcessInstanceService {

    public ProcessInstance create(String processInstanceName,
                                  ProcessDefinition processDefinition,
                                  String operator,
                                  ProcessAction startAction,
                                  String remark) {
        ProcessNode rootNode = processDefinition.rootProcessNode();
        return ProcessInstance.start(
                IdUtil.fastUUID(),
                processInstanceName,
                processDefinition.getProcessDefinitionId(),
                operator,
                startAction,
                rootNode,
                remark
        );
    }
}
