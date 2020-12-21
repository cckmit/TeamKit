package org.team4u.workflow.domain.definition;

import org.team4u.ddd.domain.model.ValueObject;

import java.util.Objects;

/**
 * 流程动作
 *
 * @author jay.wu
 */
public class ProcessAction extends ValueObject {
    /**
     * 动作标识
     */
    private final String actionId;
    /**
     * 动作名称
     */
    private final String actionName;

    public ProcessAction(String actionId, String actionName) {
        this.actionId = actionId;
        this.actionName = actionName;
    }

    @Override
    public String toString() {
        return getActionId();
    }

    public String getActionId() {
        return actionId;
    }

    public String getActionName() {
        return actionName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessAction action = (ProcessAction) o;
        return actionId.equals(action.actionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionId);
    }
}