package org.team4u.workflow.domain.instance;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.Log;
import org.team4u.base.error.SystemDataNotExistException;
import org.team4u.base.lang.IdObjectService;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.instance.node.handler.*;

import java.util.List;

/**
 * 流程节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessNodeHandlers extends IdObjectService<String, ProcessNodeHandler> {

    private final Log log = Log.get();

    public ProcessNodeHandlers() {
        this(CollUtil.newArrayList(
                new StaticNodeHandler(),
                new AssigneeNodeHandler(),
                new ActionChoiceNodeHandler(),
                new AssigneeActionChoiceNodeHandler()
        ));
    }

    public ProcessNodeHandlers(List<ProcessNodeHandler> objects) {
        super(objects);
    }

    public void handle(ProcessNodeHandler.Context context) {
        if (context.getInstance() == null) {
            throw new SystemDataNotExistException("无法找到当前流程实例");
        }

        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "handle")
                .append("instance", context.getInstance().toString())
                .append("operatorId", context.getOperatorId())
                .append("action", context.getAction().toString())
                .append("startNode", context.getInstance().getCurrentNode())
                .append("definition", context.getDefinition())
                .append("operatorPermissions", context.getOperatorPermissions());

        ProcessNode nextNode = context.getDefinition().processNodeOf(
                context.getInstance()
                        .getCurrentNode()
                        .getNextNodeId()
        );

        if (nextNode == null) {
            throw new SystemDataNotExistException(lm.fail("nextNode is null").toString());
        }

        while (nextNode != null) {
            lm.append("nextNode", nextNode);
            log.info(lm.success().toString());

            nextNode = context.getDefinition().processNodeOf(
                    availableObjectOfId(nextNode.getClass().getSimpleName())
                            .handle(context.setNode(nextNode))
            );
        }
    }
}