package org.team4u.workflow.domain.instance;

import org.team4u.workflow.domain.instance.node.handler.ProcessNodeHandler;

import java.util.Set;

/**
 * 流程权限服务
 *
 * @author jay.wu
 */
public interface ProcessPermissionService {

    /**
     * 获取当前处理人权限集合
     *
     * @param context 上下文
     * @return 权限集合
     */
    Set<String> operatorPermissionsOf(ProcessNodeHandler.Context context);
}