package org.team4u.workflow.domain.definition;

import org.team4u.ddd.domain.model.ValueObject;

import java.util.List;
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
    /**
     * 动作所需权限集合
     */
    private final List<String> requiredPermissions;

    public ProcessAction(String actionId, String actionName, List<String> requiredPermissions) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.requiredPermissions = requiredPermissions;
    }

    public String getActionId() {
        return actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public List<String> getRequiredPermissions() {
        return requiredPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProcessAction that = (ProcessAction) o;
        return actionId.equals(that.actionId) && actionName.equals(that.actionName) && Objects.equals(requiredPermissions, that.requiredPermissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionId, actionName, requiredPermissions);
    }

    @Override
    public String toString() {
        return actionId;
    }
}