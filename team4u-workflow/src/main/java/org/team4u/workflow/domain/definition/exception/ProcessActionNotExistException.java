package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 流程动作不存在异常
 *
 * @author jay.wu
 */
public class ProcessActionNotExistException extends SystemDataNotExistException {

    public ProcessActionNotExistException(String message) {
        super(message);
    }
}
