package org.team4u.workflow.application.model;

import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.form.ProcessForm;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.List;

/**
 * 流程表单模型
 *
 * @author jay.wu
 */
public class ProcessFormModel {

    /**
     * 流程表单
     */
    private ProcessForm form;
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
    public <F extends ProcessForm> F getForm() {
        return (F) form;
    }

    public void setForm(ProcessForm form) {
        this.form = form;
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
                "form=" + form +
                ", instance=" + instance +
                ", actions=" + actions +
                ", events=" + events +
                '}';
    }
}