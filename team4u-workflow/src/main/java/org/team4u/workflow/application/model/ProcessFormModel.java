package org.team4u.workflow.application.model;

import lombok.Data;
import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.List;
import java.util.Set;

/**
 * 表单索引模型
 *
 * @author jay.wu
 */
@Data
public class ProcessFormModel {
    /**
     * 表单索引
     */
    private FormIndex formIndex;
    /**
     * 流程实例
     */
    private ProcessInstance instance;
    /**
     * 当前处理人可用动作集合
     */
    private List<ProcessAction> actions;
    /**
     * 当前处理人拥有权限集合
     */
    private Set<String> permissions;
    /**
     * 流程节点变更事件集合
     */
    private List<ProcessNodeChangedEvent> events;

    @SuppressWarnings("unchecked")
    public <F extends FormIndex> F getFormIndex() {
        return (F) formIndex;
    }
}