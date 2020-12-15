package org.team4u.workflow.domain.instance;

import cn.hutool.core.util.StrUtil;
import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

import java.util.HashSet;
import java.util.Set;

import static org.team4u.workflow.domain.definition.ProcessAction.Permission.*;

/**
 * 默认流程权限服务
 *
 * @author jay.wu
 */
public class DefaultProcessPermissionService implements ProcessPermissionService {

    @Override
    public Set<String> operatorPermissionsOf(ProcessNodeHandler.Context context) {
        Set<String> permissions = new HashSet<>();

        if (context.getInstance().getLogs()
                .stream()
                .anyMatch(it -> StrUtil.equals(it.getCreateBy(), context.getOperatorId()))) {
            permissions.add(VIEW.name());
        }

        if (StrUtil.equals(context.getInstance().getCreateBy(), context.getOperatorId())) {
            permissions.add(EDIT.name());
        }

        if (context.getInstance().getAssignees()
                .stream()
                .anyMatch(it -> StrUtil.equals(it.getAssignee(), context.getOperatorId()))) {
            permissions.add(REVIEW.name());
        }

        return permissions;
    }
}