package org.team4u.workflow.domain.form.process.definition;

import cn.hutool.core.collection.CollUtil;
import org.team4u.workflow.domain.definition.ProcessAction;

import java.util.List;
import java.util.Set;

/**
 * 表单索引动作
 *
 * @author jay.wu
 */
public class ProcessFormAction extends ProcessAction {
    /**
     * 动作所需权限集合
     */
    private final List<String> requiredPermissions;

    public ProcessFormAction(String actionId, String actionName, List<String> requiredPermissions) {
        super(actionId, actionName);
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

    /**
     * 判断是否存在所需权限
     */
    public boolean hasPermission(String permission) {
        return CollUtil.contains(getRequiredPermissions(), permission);
    }

    public List<String> getRequiredPermissions() {
        return requiredPermissions;
    }

    /**
     * 表单动作权限
     */
    public enum Permission {
        /**
         * 编辑
         */
        EDIT,
        /**
         * 审批
         */
        REVIEW,
        /**
         * 查看
         */
        VIEW
    }
}