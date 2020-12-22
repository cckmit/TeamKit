package org.team4u.workflow.application;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import org.team4u.workflow.application.command.AbstractHandleProcessInstanceCommand;
import org.team4u.workflow.application.command.CreateProcessInstanceCommand;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.definition.exception.ProcessDefinitionNotExistException;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceNotExistException;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;

/**
 * 工作流应用服务
 *
 * @author jay.wu
 */
public class ProcessAppService {

    private final ProcessNodeHandlers processNodeHandlers;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public ProcessAppService(ProcessNodeHandlers processNodeHandlers,
                             ProcessInstanceRepository processInstanceRepository,
                             ProcessDefinitionRepository processDefinitionRepository) {
        this.processNodeHandlers = processNodeHandlers;
        this.processInstanceRepository = processInstanceRepository;
        this.processDefinitionRepository = processDefinitionRepository;
    }

    /**
     * 获取有效的流程定义
     *
     * @param processDefinitionId 流程定义标识
     * @return 流程定义
     */
    public ProcessDefinition processDefinitionOf(String processDefinitionId) {
        return processDefinitionRepository.domainOf(processDefinitionId);
    }

    /**
     * 可用的获取流程定义
     *
     * @param processDefinitionId 流程定义标识
     * @return 流程定义，无法获取则返回ProcessDefinitionNotExistException
     */
    public ProcessDefinition availableProcessDefinitionOf(String processDefinitionId) {
        ProcessDefinition definition = processDefinitionOf(processDefinitionId);

        if (definition == null) {
            throw new ProcessDefinitionNotExistException(processDefinitionId);
        }

        return definition;
    }

    /**
     * 获取有效的流程实例
     *
     * @param processInstanceId 流程实例标识
     * @return 流程实例，无法获取则返回ProcessInstanceNotExistException
     */
    public ProcessInstance availableProcessInstanceOf(String processInstanceId) {
        ProcessInstance instance = processInstanceOf(processInstanceId);

        if (instance == null) {
            throw new ProcessInstanceNotExistException(processInstanceId);
        }

        return instance;
    }

    /**
     * 获取流程实例
     *
     * @param processInstanceId 流程实例标识
     * @return 流程实例
     */
    public ProcessInstance processInstanceOf(String processInstanceId) {
        return processInstanceRepository.domainOf(processInstanceId);
    }

    /**
     * 创建流程实例
     *
     * @param command 开始命令参数
     * @return 流程实例
     */
    public ProcessInstance create(CreateProcessInstanceCommand command) {
        ProcessDefinition definition = processDefinitionRepository.domainOf(command.getProcessDefinitionId());

        ProcessInstance instance = ProcessInstance.create(
                ObjectUtil.defaultIfBlank(command.getProcessInstanceId(), IdUtil.fastSimpleUUID()),
                command.getProcessInstanceName(),
                ProcessDefinitionId.of(command.getProcessDefinitionId()),
                command.getOperatorId(),
                definition.rootNode()
        );

        return handle(command, definition, instance);
    }

    /**
     * 开始流程实例
     *
     * @param command 开始命令参数
     * @return 流程实例
     */
    public ProcessInstance start(StartProcessInstanceCommand command) {
        ProcessInstance instance = availableProcessInstanceOf(command.getProcessInstanceId());

        ProcessDefinition definition = processDefinitionRepository.domainOf(
                instance.getProcessDefinitionId().toString()
        );

        return handle(command, definition, instance);
    }

    /**
     * 处理流程实例
     *
     * @param command 开始命令参数
     * @return 流程实例
     */
    private ProcessInstance handle(AbstractHandleProcessInstanceCommand command,
                                   ProcessDefinition definition,
                                   ProcessInstance instance) {
        ProcessAction action = definition.availableActionOf(command.getActionId());

        processNodeHandlers.handle(ProcessNodeHandlerContext.Builder.create()
                .withInstance(instance)
                .withDefinition(definition)
                .withAction(action)
                .withOperatorId(command.getOperatorId())
                .withRemark(command.getRemark())
                .withExt(command.getExt())
                .build());

        processInstanceRepository.save(instance);
        return instance;
    }
}