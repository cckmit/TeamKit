package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.DataNotExistException;

/**
 * 流程动作不存在异常
 *
 * @author jay.wu
 */
public class ProcessActionNotExistException extends DataNotExistException {

    public ProcessActionNotExistException(String message) {
        super(message);
    }
}
