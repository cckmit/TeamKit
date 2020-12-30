package org.team4u.workflow.domain.instance.exception;

import org.team4u.workflow.domain.ProcessSystemException;

/**
 * 流程节点处理器不存在异常
 *
 * @author jay.wu
 */
public class ProcessNodeHandlerNotExistException extends ProcessSystemException {

    public ProcessNodeHandlerNotExistException(String message) {
        super(message);
    }
}