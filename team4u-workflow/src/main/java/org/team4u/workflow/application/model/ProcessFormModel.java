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

    private ProcessForm form;
    private ProcessInstance instance;
    private List<ProcessAction> actions;
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