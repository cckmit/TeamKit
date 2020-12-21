package org.team4u.workflow.application;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.ddd.event.EventStore;
import org.team4u.ddd.event.StoredEvent;
import org.team4u.workflow.application.command.AbstractHandleProcessInstanceCommand;
import org.team4u.workflow.application.command.CreateProcessInstanceCommand;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 工作流应用服务
 *
 * @author jay.wu
 */
public class ProcessAppService {

    private final EventStore eventStore;

    private final ProcessNodeHandlers processNodeHandlers;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public ProcessAppService(EventStore eventStore,
                             ProcessNodeHandlers processNodeHandlers,
                             ProcessInstanceRepository processInstanceRepository,
                             ProcessDefinitionRepository processDefinitionRepository) {
        this.eventStore = eventStore;
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
     * 创建流程实例
     *
     * @param command 开始命令参数
     * @return 流程实例
     */
    public ProcessInstance create(CreateProcessInstanceCommand command) {
        ProcessDefinition definition = processDefinitionRepository.domainOf(command.getProcessDefinitionId());

        ProcessInstance instance = ProcessInstance.create(
                IdUtil.fastSimpleUUID(),
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
        ProcessInstance instance = processInstanceOf(command.getProcessInstanceId());

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
    public ProcessInstance handle(AbstractHandleProcessInstanceCommand command,
                                  ProcessDefinition definition,
                                  ProcessInstance instance) {
        ProcessAction action = definition.availableActionOf(command.getActionId());

        processNodeHandlers.handle(new ProcessNodeHandler.Context(
                instance,
                definition,
                action,
                command.getOperatorId(),
                command.getRemark(),
                command.getExt()
        ));

        processInstanceRepository.save(instance);
        return instance;
    }


    public List<ProcessNodeChangedEvent> processNodeChangedEventsOf(String processInstanceId) {
        if (StrUtil.isBlank(processInstanceId)) {
            return Collections.emptyList();
        }

        return eventStore.allStoredEventsOf(processInstanceId)
                .stream()
                .filter(it -> StrUtil.equals(it.typeName(), ProcessNodeChangedEvent.class.getName()))
                .map(StoredEvent::<ProcessNodeChangedEvent>toDomainEvent)
                .collect(Collectors.toList());
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