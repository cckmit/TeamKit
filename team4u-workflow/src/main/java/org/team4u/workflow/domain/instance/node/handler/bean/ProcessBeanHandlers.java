package org.team4u.workflow.domain.instance.node.handler.bean;

import org.team4u.base.registrar.PolicyRegistrar;
import org.team4u.workflow.domain.instance.exception.ProcessBeanHandlerNotExistException;

import java.util.List;

/**
 * 流程bean节点处理器服务
 *
 * @author jay.wu
 */
public class ProcessBeanHandlers extends PolicyRegistrar<String, ProcessBeanHandler> {

    public ProcessBeanHandlers() {
        registerPoliciesByBeanProvidersAndEvent();
    }

    public ProcessBeanHandlers(List<ProcessBeanHandler> objects) {
        super(objects);
    }

    public ProcessBeanHandler getBean(String beanName) {
        ProcessBeanHandler handler = policyOf(beanName);

        if (handler == null) {
            throw new ProcessBeanHandlerNotExistException(beanName);
        }

        return handler;
    }
}