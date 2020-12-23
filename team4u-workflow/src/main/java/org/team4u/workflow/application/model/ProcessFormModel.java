package org.team4u.workflow.application.model;

import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.form.FormIndex;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.List;

/**
 * 表单索引模型
 *
 * @author jay.wu
 */
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
     * 流程节点变更事件集合
     */
    private List<ProcessNodeChangedEvent> events;

    @SuppressWarnings("unchecked")
    public <F extends FormIndex> F getFormIndex() {
        return (F) formIndex;
    }

    public void setFormIndex(FormIndex formIndex) {
        this.formIndex = formIndex;
    }

    public ProcessInstance getInstance() {
        return instance;
    }

    public void setInstance(ProcessInstance instance) {
        this.instance = instance;
    }

    public List<ProcessAction> getActions() {
        return actions;
    }

    public void setActions(List<ProcessAction> actions) {
        this.actions = actions;
    }

    public List<ProcessNodeChangedEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ProcessNodeChangedEvent> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return "ProcessFormModel{" +
                "index=" + formIndex +
                ", instance=" + instance +
                ", actions=" + actions +
                ", events=" + events +
                '}';
    }
}