package org.team4u.workflow.domain.instance;

import org.team4u.workflow.domain.definition.ProcessAction;

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
    Set<String> operatorPermissionsOf(Context context);

    class Context {
        private final ProcessInstance instance;
        private final ProcessAction action;
        private final String operatorId;

        public Context(ProcessInstance instance, ProcessAction action, String operatorId) {
            this.instance = instance;
            this.action = action;
            this.operatorId = operatorId;
        }

        public ProcessInstance getInstance() {
            return instance;
        }

        public ProcessAction getAction() {
            return action;
        }

        public String getOperatorId() {
            return operatorId;
        }
    }
}