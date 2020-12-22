package org.team4u.workflow.domain.instance.node.handler;

import org.team4u.base.lang.IdObjectService;
import org.team4u.workflow.domain.instance.exception.ProcessBeanHandlerNotExistException;

import java.util.List;

/**
 * 流程bean节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessBeanHandlers extends IdObjectService<String, ProcessBeanHandler> {

    public ProcessBeanHandlers() {
        super(ProcessBeanHandler.class);
    }

    public ProcessBeanHandlers(List<ProcessBeanHandler> objects) {
        super(objects);
    }

    public ProcessBeanHandler getBean(String beanName) {
        ProcessBeanHandler handler = objectOfId(beanName);

        if (handler == null) {
            throw new ProcessBeanHandlerNotExistException(beanName);
        }

        return handler;
    }
}