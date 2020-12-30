package org.team4u.workflow.domain.instance.exception;

import org.team4u.workflow.domain.ProcessSystemException;

/**
 * 流程实例不存在异常
 *
 * @author jay.wu
 */
public class ProcessInstanceNotExistException extends ProcessSystemException {

    public ProcessInstanceNotExistException(String message) {
        super(message);
    }
}
