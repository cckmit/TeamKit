package org.team4u.workflow.application;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.ProcessPermissionService;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 工作流应用服务
 *
 * @author jay.wu
 */
public class WorkflowAppService {

    private final ProcessNodeHandlers processNodeHandlers;
    private final ProcessPermissionService processPermissionService;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public WorkflowAppService(ProcessNodeHandlers processNodeHandlers,
                              ProcessPermissionService processPermissionService,
                              ProcessInstanceRepository processInstanceRepository,
                              ProcessDefinitionRepository processDefinitionRepository) {
        this.processNodeHandlers = processNodeHandlers;
        this.processPermissionService = processPermissionService;
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
        ProcessInstance instance;

        if (StrUtil.isBlank(command.getProcessInstanceId())) {
            // 新建流程
            instance = ProcessInstance.create(
                    IdUtil.fastUUID(),
                    command.getProcessInstanceName(),
                    command.getProcessDefinitionId(),
                    command.getOperatorId(),
                    definition.rootNode()
            );
        } else {
            // 已有流程
            instance = processInstanceOf(command.getProcessInstanceId());
        }

        Set<String> permissions = processPermissionService.operatorPermissionsOf(
                new ProcessPermissionService.Context(
                        instance, command.getAction(), command.getOperatorId()
                )
        );
        processNodeHandlers.handle(new ProcessNodeHandler.Context(
                instance,
                definition,
                command.getAction(),
                command.getOperatorId(),
                permissions,
                command.getRemark()
        ));

        processInstanceRepository.save(instance);
        return instance;
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

    /**
     * 当前流程实例可用的动作集合
     */
    public List<ProcessAction> availableActionsOf(ProcessInstance instance, String operatorId) {
        ProcessDefinition definition = processDefinitionRepository.domainOf(instance.getProcessDefinitionId());
        ProcessNode nextNode = definition.processNodeOf(instance.getCurrentNode().getNextNodeId());

        if (!(nextNode instanceof ActionChoiceNode)) {
            return Collections.emptyList();
        }

        Set<String> permissions = processPermissionService.operatorPermissionsOf(
                new ProcessPermissionService.Context(
                        instance, null, operatorId
                )
        );

        return ((ActionChoiceNode) nextNode).getActionNodes()
                .stream()
                .map(it -> definition.actionOf(it.getActionId()))
                .filter(it -> it.matchPermissions(permissions))
                .collect(Collectors.toList());
    }
}