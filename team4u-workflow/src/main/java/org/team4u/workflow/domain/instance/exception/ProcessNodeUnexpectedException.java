package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.SystemException;

/**
 * 流程实例不符合预期异常
 *
 * @author jay.wu
 */
public class ProcessNodeUnexpectedException extends SystemException {

    public ProcessNodeUnexpectedException(String message) {
        super(message);
    }
}
