package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.DataNotExistException;

/**
 * 流程bean处理器不存在异常
 *
 * @author jay.wu
 */
public class ProcessBeanHandlerNotExistException extends DataNotExistException {

    public ProcessBeanHandlerNotExistException(String message) {
        super(message);
    }
}