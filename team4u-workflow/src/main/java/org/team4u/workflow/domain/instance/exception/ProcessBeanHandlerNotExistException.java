package org.team4u.workflow.domain.instance.exception;

import org.team4u.workflow.domain.ProcessSystemException;

/**
 * 流程bean处理器不存在异常
 *
 * @author jay.wu
 */
public class ProcessBeanHandlerNotExistException extends ProcessSystemException {

    public ProcessBeanHandlerNotExistException(String message) {
        super(message);
    }
}