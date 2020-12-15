package org.team4u.workflow.application;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

/**
 * 工作流应用服务
 *
 * @author jay.wu
 */
public class WorkflowAppService {

    private final ProcessNodeHandlers processNodeHandlers;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public WorkflowAppService(ProcessNodeHandlers processNodeHandlers,
                              ProcessInstanceRepository processInstanceRepository,
                              ProcessDefinitionRepository processDefinitionRepository
    ) {
        this.processNodeHandlers = processNodeHandlers;
        this.processInstanceRepository = processInstanceRepository;
        this.processDefinitionRepository = processDefinitionRepository;
    }

    /**
     * 获取流程定义
     *
     * @param processDefinitionId 流程定义标识
     * @return 流程定义
     */
    public ProcessDefinition processDefinitionOf(String processDefinitionId) {
        ProcessDefinition definition = processDefinitionRepository.domainOf(processDefinitionId);

        if (definition == null) {
            throw new SystemDataNotExistException("ProcessDefinition|id=" + processDefinitionId);
        }

        return definition;
    }

    /**
     * 开始流程实例
     *
     * @param command 开始命令参数
     * @return 流程实例
     */
    public ProcessInstance start(StartProcessInstanceCommand command) {
        ProcessDefinition definition = processDefinitionRepository.domainOf(command.getProcessDefinitionId());
        ProcessInstance processInstance;

        if (StrUtil.isBlank(command.getProcessInstanceId())) {
            // 新建流程
            processInstance = ProcessInstance.create(
                    IdUtil.fastUUID(),
                    command.getProcessInstanceName(),
                    command.getProcessDefinitionId(),
                    command.getOperator(),
                    definition.rootNode()
            );
        } else {
            // 已有流程
            processInstance = processInstanceOf(command.getProcessInstanceId());
        }

        processNodeHandlers.handle(new ProcessNodeHandler.Context(
                processInstance,
                definition,
                command.getAction(),
                command.getOperator(),
                command.getOperatorPermissions(),
                command.getRemark()
        ));

        processInstanceRepository.save(processInstance);
        return processInstance;
    }

    /**
     * 获取流程实例
     *
     * @param processInstanceId 流程实例标识
     * @return 流程实例
     */
    ProcessInstance processInstanceOf(String processInstanceId) {
        ProcessInstance instance = processInstanceRepository.domainOf(processInstanceId);

        if (instance == null) {
            throw new SystemDataNotExistException("ProcessInstance|id=" + processInstanceId);
        }

        return instance;
    }
}