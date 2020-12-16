package org.team4u.workflow.domain.instance;

import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.List;
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
        private final List<ProcessNodeChangedEvent> changedEvents;
        private final ProcessAction action;
        private final String operatorId;

        public Context(ProcessInstance instance,
                       List<ProcessNodeChangedEvent> changedEvents,
                       ProcessAction action,
                       String operatorId) {
            this.instance = instance;
            this.changedEvents = changedEvents;
            this.action = action;
            this.operatorId = operatorId;
        }

        public ProcessInstance getInstance() {
            return instance;
        }

        public List<ProcessNodeChangedEvent> getChangedEvents() {
            return changedEvents;
        }

        public ProcessAction getAction() {
            return action;
        }

        public String getOperatorId() {
            return operatorId;
        }
    }
}