package org.team4u.workflow.domain.form;

import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;

import java.util.HashSet;
import java.util.Set;

import static org.team4u.workflow.domain.form.process.definition.ProcessFormAction.Permission.*;


/**
 * 默认流程权限服务
 *
 * @author jay.wu
 */
public class DefaultFormPermissionService implements FormPermissionService {

    @Override
    public Set<String> operatorPermissionsOf(Context context) {
        Set<String> permissions = new HashSet<>();

        if (canView(context)) {
            permissions.add(VIEW.name());
        }

        if (canEdit(context)) {
            permissions.add(EDIT.name());
        }

        if (canReview(context)) {
            permissions.add(REVIEW.name());
        }

        return customPermissions(context, permissions);
    }

    @Override
    public boolean shouldSaveProcessForm(ProcessFormAction action) {
        if (action == null) {
            return true;
        }

        return action.hasPermission(ProcessFormAction.Permission.EDIT.name());
    }

    /**
     * 自定义权限
     *
     * @param context     上下文
     * @param permissions 已有权限集合
     * @return 新权限结合
     */
    protected Set<String> customPermissions(Context context, Set<String> permissions) {
        return permissions;
    }

    protected boolean canView(Context context) {
        if (canEdit(context) || canReview(context)) {
            return true;
        }

        return context.getChangedEvents()
                .stream()
                .anyMatch(it -> StrUtil.equals(it.getOperator(), context.getOperatorId()));
    }

    protected boolean canEdit(Context context) {
        // 仅新建表单时可编辑
        return context.getInstance() == null || context.getInstance().getId() == null;
    }

    protected boolean canReview(Context context) {
        if (context.getInstance() == null) {
            return false;
        }

        return context.getInstance().getAssignees()
                .stream()
                .anyMatch(it -> StrUtil.equals(it.getAssignee(), context.getOperatorId()));
    }
}