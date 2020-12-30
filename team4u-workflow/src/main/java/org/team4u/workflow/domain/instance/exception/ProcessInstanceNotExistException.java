package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 流程实例不存在异常
 *
 * @author jay.wu
 */
public class ProcessInstanceNotExistException extends SystemDataNotExistException {

    public ProcessInstanceNotExistException(String message) {
        super(message);
    }
}
