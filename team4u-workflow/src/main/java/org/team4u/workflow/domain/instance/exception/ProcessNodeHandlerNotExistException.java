package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 流程节点处理器不存在异常
 *
 * @author jay.wu
 */
public class ProcessNodeHandlerNotExistException extends SystemDataNotExistException {

    public ProcessNodeHandlerNotExistException(String message) {
        super(message);
    }
}