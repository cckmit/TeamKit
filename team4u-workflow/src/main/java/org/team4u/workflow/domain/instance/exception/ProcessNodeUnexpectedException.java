package org.team4u.workflow.domain.instance.exception;

import org.team4u.workflow.domain.ProcessSystemException;

/**
 * 流程实例不符合预期异常
 *
 * @author jay.wu
 */
public class ProcessNodeUnexpectedException extends ProcessSystemException {

    public ProcessNodeUnexpectedException(String message) {
        super(message);
    }
}
