package org.team4u.workflow.domain.definition;

import cn.hutool.core.collection.CollUtil;
import org.team4u.ddd.domain.model.ValueObject;

import java.util.Objects;
import java.util.Set;

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
    private final Set<String> requiredPermissions;

    public ProcessAction(String actionId, String actionName, Set<String> requiredPermissions) {
        this.actionId = actionId;
        this.actionName = actionName;
        this.requiredPermissions = requiredPermissions;
    }

    /**
     * 判断给定权限是否满足动作所需权限
     *
     * @param permissions 给定权限
     * @return true：满足，false：不满足
     */
    public boolean matchPermissions(Set<String> permissions) {
        return CollUtil.containsAll(permissions, getRequiredPermissions());
    }

    public String getActionId() {
        return actionId;
    }

    public String getActionName() {
        return actionName;
    }

    public Set<String> getRequiredPermissions() {
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

    public enum Permission {
        /**
         * 创建者
         */
        EDIT,
        /**
         * 审批权限
         */
        REVIEW,
        /**
         * 查看权限
         */
        VIEW
    }
}