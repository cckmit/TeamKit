package org.team4u.workflow.application;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import org.team4u.workflow.application.command.CreateProcessInstanceCommand;
import org.team4u.workflow.application.command.StartProcessInstanceCommand;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.exception.ProcessDefinitionNotExistException;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.ProcessInstanceRepository;
import org.team4u.workflow.domain.instance.ProcessNodeHandlers;
import org.team4u.workflow.domain.instance.exception.ProcessInstanceNotExistException;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandlerContext;
import org.team4u.workflow.domain.instance.node.handler.bean.ProcessBeanHandler;

import java.util.List;

/**
 * 工作流应用服务
 *
 * @author jay.wu
 */
public class ProcessAppService {

    private final ProcessNodeHandlers processNodeHandlers;
    private final ProcessInstanceRepository processInstanceRepository;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public ProcessAppService(ProcessInstanceRepository processInstanceRepository,
                             ProcessDefinitionRepository processDefinitionRepository) {
        this.processInstanceRepository = processInstanceRepository;
        this.processDefinitionRepository = processDefinitionRepository;

        this.processNodeHandlers = new ProcessNodeHandlers(processInstanceRepository);
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
     * @return 流程定义，无法获取则抛出ProcessDefinitionNotExistException
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
     * @return 流程实例，无法获取则抛出ProcessInstanceNotExistException
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
     * 获取指定流程节点处理器
     *
     * @param nodeClass 流程节点处理器类型
     * @return 流程节点处理器
     */
    @SuppressWarnings("unchecked")
    public <T extends ProcessNodeHandler> T processNodeHandlerOf(Class<? extends ProcessNode> nodeClass) {
        return (T) processNodeHandlers.policiesOf(nodeClass);
    }

    /**
     * 获取指定流程bean处理器
     *
     * @param handlerId 流程bean处理器标识
     * @return 流程bean处理器
     */
    @SuppressWarnings("unchecked")
    public <T extends ProcessBeanHandler> T processBeanHandlerOf(String handlerId) {
        return (T) processNodeHandlers.beanHandlers().policiesOf(handlerId);
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
                command.getProcessInstanceType(),
                command.getProcessInstanceName(),
                ProcessDefinitionId.of(command.getProcessDefinitionId()),
                command.getOperatorId(),
                definition.rootNode(),
                command.getProcessInstanceDetail()
        );

        processInstanceRepository.save(instance);
        return instance;
    }

    /**
     * 开始流程实例
     *
     * @param command 开始命令参数
     * @return 流程实例
     */
    public ProcessInstance start(StartProcessInstanceCommand command) {
        ProcessInstance instance = availableProcessInstanceOf(command.getProcessInstanceId());

        if (command.getProcessInstanceDetail() != null) {
            instance.setProcessInstanceDetail(command.getProcessInstanceDetail());
        }

        ProcessDefinition definition = processDefinitionRepository.domainOf(
                instance.getProcessDefinitionId().toString()
        );
        processNodeHandlers.handle(ProcessNodeHandlerContext.Builder.create()
                .withInstance(instance)
                .withDefinition(definition)
                .withAction(definition.availableActionOf(command.getActionId()))
                .withOperatorId(command.getOperatorId())
                .withRemark(command.getRemark())
                .withExt(command.getExt())
                .build());

        return instance;
    }

    /**
     * 保存流程定义
     */
    public void saveDefinition(ProcessDefinition definition) {
        processDefinitionRepository.save(definition);
    }

    /**
     * 注册流程节点处理器
     *
     * @param handler 流程节点处理器
     */
    public void registerNodeHandler(ProcessNodeHandler handler) {
        processNodeHandlers.registerPolicy(handler);
    }

    /**
     * 注册流程bean处理器
     *
     * @param handler 流程bean处理器
     */
    public void registerBeanHandler(ProcessBeanHandler handler) {
        processNodeHandlers.beanHandlers().registerPolicy(handler);
    }

    /**
     * 注册流程bean处理器集合
     *
     * @param handlers 流程bean处理器集合
     */
    public void registerBeanHandlers(List<ProcessBeanHandler> handlers) {
        for (ProcessBeanHandler handler : handlers) {
            registerBeanHandler(handler);
        }
    }

    /**
     * 获取流程节点处理器服务
     */
    public ProcessNodeHandlers processNodeHandlers() {
        return processNodeHandlers;
    }
}