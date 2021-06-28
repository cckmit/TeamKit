package org.team4u.workflow.application;

import cn.hutool.db.PageResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.team4u.workflow.application.command.InstancesQuery;
import org.team4u.workflow.domain.definition.ProcessDefinition;
import org.team4u.workflow.domain.definition.ProcessDefinitionId;
import org.team4u.workflow.domain.definition.ProcessDefinitionRepository;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceConverter;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceDo;
import org.team4u.workflow.infrastructure.persistence.instance.ProcessInstanceMapper;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工作流应用查询服务
 *
 * @author jay.wu
 */
public class ProcessAppQueryService {

    private final ProcessInstanceMapper processInstanceMapper;
    private final ProcessDefinitionRepository processDefinitionRepository;

    public ProcessAppQueryService(ProcessInstanceMapper processInstanceMapper,
                                  ProcessDefinitionRepository processDefinitionRepository) {
        this.processInstanceMapper = processInstanceMapper;
        this.processDefinitionRepository = processDefinitionRepository;
    }

    /**
     * 获取待审批流程实例集合
     *
     * @param query 查询条件
     * @return 待审批流程实例集合
     */
    public List<ProcessInstance> instancesOfPending(InstancesQuery query) {
        return toPageResult(
                query.getPageNumber(),
                query.getPageSize(),
                page -> processInstanceMapper.instancesOfPending(page, query)
        );
    }

    /**
     * 获取申请流程实例集合
     *
     * @param query 查询条件
     * @return 申请流程实例集合
     */
    public List<ProcessInstance> instancesOfHistory(InstancesQuery query) {
        return toPageResult(
                query.getPageNumber(),
                query.getPageSize(),
                page -> processInstanceMapper.instancesOfHistory(page, query)
        );
    }

    /**
     * 获取历史审批流程实例集合
     *
     * @param query 查询条件
     * @return 历史审批流程实例集合
     */
    public PageResult<ProcessInstance> instancesOfApply(InstancesQuery query) {
        return toPageResult(
                query.getPageNumber(),
                query.getPageSize(),
                page -> processInstanceMapper.instancesOfApply(page, query)
        );
    }

    private PageResult<ProcessInstance> toPageResult(int pageNumber, int pageSize,
                                                     Function<Page<ProcessInstanceDo>, IPage<ProcessInstanceDo>> worker) {
        IPage<ProcessInstanceDo> page = worker.apply(
                new Page<ProcessInstanceDo>()
                        .setCurrent(pageNumber)
                        .setSize(pageSize)
        );

        PageResult<ProcessInstance> result = new PageResult<>(pageNumber - 1, pageSize, (int) page.getTotal());
        result.addAll(page.getRecords().stream()
                .map(it -> {
                    ProcessDefinition definition = processDefinitionRepository.domainOf(new ProcessDefinitionId(
                            it.getProcessDefinitionId(),
                            it.getProcessDefinitionVersion()
                    ).toString());
                    return ProcessInstanceConverter.instance().toProcessInstance(definition, it, null);
                }).collect(Collectors.toList()));
        return result;
    }
}