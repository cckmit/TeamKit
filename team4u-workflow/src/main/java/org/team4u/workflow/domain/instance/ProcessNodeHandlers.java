package org.team4u.workflow.domain.instance;

import cn.hutool.log.Log;
import org.team4u.base.lang.IdObjectService;
import org.team4u.base.log.LogMessage;
import org.team4u.workflow.domain.definition.ProcessNode;
import org.team4u.workflow.domain.definition.exception.ProcessNodeNotExistException;
import org.team4u.workflow.domain.instance.exception.ProcessInstanceNotExistException;
import org.team4u.workflow.domain.instance.node.handler.*;

/**
 * 流程节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessNodeHandlers extends IdObjectService<String, ProcessNodeHandler> {

    private final Log log = Log.get();

    private final ProcessBeanHandlers beanHandlers;

    public ProcessNodeHandlers() {
        super(ProcessNodeHandler.class);

        beanHandlers = new ProcessBeanHandlers();

        initProcessNodeHandler();
    }

    public void handle(ProcessNodeHandlerContext context) {
        if (context.getInstance() == null) {
            throw new ProcessInstanceNotExistException("context instance is null");
        }

        LogMessage lm = LogMessage.create(this.getClass().getSimpleName(), "handle")
                .append("instance", context.getInstance().toString())
                .append("operatorId", context.getOperatorId())
                .append("action", context.getAction().toString())
                .append("startNode", context.getInstance().getCurrentNode())
                .append("definition", context.getDefinition())
                .append("ext", context.getExt());

        ProcessNode nextNode = context.getDefinition().processNodeOf(
                context.getInstance()
                        .getCurrentNode()
                        .getNextNodeId()
        );

        if (nextNode == null) {
            throw new ProcessNodeNotExistException(lm.fail("nextNode is null").toString());
        }

        while (nextNode != null) {
            lm.append("nextNode", nextNode);
            log.info(lm.success().toString());

            nextNode = context.getDefinition().processNodeOf(
                    availableObjectOfId(nextNode.getClass().getName())
                            .handle(context.setNode(nextNode))
            );
        }
    }

    public ProcessBeanHandlers beanHandlers() {
        return beanHandlers;
    }

    private void initProcessNodeHandler() {
        saveIdObject(new BeanNodeHandler(beanHandlers));
        saveIdObject(new BeanChoiceNodeHandler(beanHandlers));
    }
}