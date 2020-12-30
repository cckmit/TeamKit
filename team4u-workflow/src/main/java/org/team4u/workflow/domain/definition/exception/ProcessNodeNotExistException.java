package org.team4u.workflow.domain.definition.exception;

import org.team4u.workflow.domain.ProcessSystemException;

/**
 * 流程节点不存在异常
 *
 * @author jay.wu
 */
public class ProcessNodeNotExistException extends ProcessSystemException {

    public ProcessNodeNotExistException(String message) {
        super(message);
    }
}