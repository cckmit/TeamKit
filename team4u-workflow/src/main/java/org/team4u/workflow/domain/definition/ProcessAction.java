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
    private String actionId;
    /**
     * 动作名称
     */
    private String actionName;
    /**
     * 动作所需权限集合
     */
    private List<String> requiredPermissions;

    public String getActionId() {
        return actionId;
    }

    public ProcessAction setActionId(String actionId) {
        this.actionId = actionId;
        return this;
    }

    public String getActionName() {
        return actionName;
    }

    public ProcessAction setActionName(String actionName) {
        this.actionName = actionName;
        return this;
    }

    public List<String> getRequiredPermissions() {
        return requiredPermissions;
    }

    public ProcessAction setRequiredPermissions(List<String> requiredPermissions) {
        this.requiredPermissions = requiredPermissions;
        return this;
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
}