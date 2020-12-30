package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 流程bean处理器不存在异常
 *
 * @author jay.wu
 */
public class ProcessBeanHandlerNotExistException extends SystemDataNotExistException {

    public ProcessBeanHandlerNotExistException(String message) {
        super(message);
    }
}