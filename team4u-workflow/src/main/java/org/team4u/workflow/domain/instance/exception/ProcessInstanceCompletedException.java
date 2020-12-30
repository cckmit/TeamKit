package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.BusinessException;

/**
 * 流程已结束异常
 *
 * @author jay.wu
 */
public class ProcessInstanceCompletedException extends BusinessException {

    public ProcessInstanceCompletedException(String message) {
        super(message);
    }
}