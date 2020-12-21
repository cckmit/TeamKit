package org.team4u.workflow.domain.form;

import org.team4u.workflow.domain.definition.ProcessAction;
import org.team4u.workflow.domain.form.process.definition.ProcessFormAction;
import org.team4u.workflow.domain.instance.ProcessInstance;
import org.team4u.workflow.domain.instance.event.ProcessNodeChangedEvent;

import java.util.List;
import java.util.Set;

/**
 * 流程权限服务
 *
 * @author jay.wu
 */
public interface ProcessFormPermissionService {

    /**
     * 获取当前处理人权限集合
     *
     * @param context 上下文
     * @return 权限集合
     */
    Set<String> operatorPermissionsOf(Context context);

    /**
     * 判断是否需要保存流程表单
     * <p>
     * 默认只有编辑权限的动作才需要保存
     *
     * @param action 当前动作
     * @return true:保存表单，反之则不保存
     */
    boolean shouldSaveProcessForm(ProcessFormAction action);

    class Context {
        private final ProcessForm form;
        private final ProcessInstance instance;
        private final List<ProcessNodeChangedEvent> changedEvents;
        private final ProcessAction action;
        private final String operatorId;

        public Context(ProcessForm form,
                       ProcessInstance instance,
                       List<ProcessNodeChangedEvent> changedEvents,
                       ProcessAction action,
                       String operatorId) {
            this.form = form;
            this.instance = instance;
            this.changedEvents = changedEvents;
            this.action = action;
            this.operatorId = operatorId;
        }

        public ProcessForm getForm() {
            return form;
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