package org.team4u.workflow.application;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.node.ActionChoiceNode;
import org.team4u.workflow.domain.definition.node.StaticNode;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.ProcessPermissionService;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;
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

    private final EventStore eventStore;

    private final ProcessNodeHandlers processNodeHandlers;
    private final ProcessPermissionService processPermissionService;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public WorkflowAppService(EventStore eventStore,
                              ProcessNodeHandlers processNodeHandlers,
                              ProcessPermissionService processPermissionService,
                              ProcessInstanceRepository processInstanceRepository,
                              ProcessDefinitionRepository processDefinitionRepository) {
        this.eventStore = eventStore;
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
        ProcessInstance instance = toProcessInstance(command, definition.rootNode());
        ProcessAction action = definition.actionOf(command.getActionId());

        processNodeHandlers.handle(new ProcessNodeHandler.Context(
                instance,
                definition,
                action,
                command.getOperatorId(),
                operatorPermissionsOf(instance, action, command.getOperatorId()),
                command.getRemark(),
                null
        ));

        processInstanceRepository.save(instance);
        return instance;
    }

    private Set<String> operatorPermissionsOf(ProcessInstance instance,
                                              ProcessAction action,
                                              String operatorId) {
        return processPermissionService.operatorPermissionsOf(
                new ProcessPermissionService.Context(
                        instance,
                        processNodeChangedEventsOf(instance.getProcessInstanceId()),
                        action,
                        operatorId
                )
        );
    }

    private List<ProcessNodeChangedEvent> processNodeChangedEventsOf(String processInstanceId) {
        if (StrUtil.isBlank(processInstanceId)) {
            return Collections.emptyList();
        }

        return eventStore.allStoredEventsOf(processInstanceId)
                .stream()
                .map(StoredEvent::<ProcessNodeChangedEvent>toDomainEvent)
                .collect(Collectors.toList());
    }

    private ProcessInstance toProcessInstance(StartProcessInstanceCommand command, StaticNode rootNode) {
        // 新建流程
        if (StrUtil.isBlank(command.getProcessInstanceId())) {
            return ProcessInstance.create(
                    IdUtil.fastUUID(),
                    command.getProcessInstanceName(),
                    command.getProcessDefinitionId(),
                    command.getOperatorId(),
                    rootNode
            );
        }

        // 已有流程
        return processInstanceOf(command.getProcessInstanceId());
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

        Set<String> permissions = operatorPermissionsOf(instance, null, operatorId);

        return ((ActionChoiceNode) nextNode).getActionNodes()
                .stream()
                .map(it -> definition.actionOf(it.getActionId()))
                .filter(it -> it.matchPermissions(permissions))
                .collect(Collectors.toList());
    }
}