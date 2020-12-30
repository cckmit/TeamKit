package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 流程节点不存在异常
 *
 * @author jay.wu
 */
public class ProcessNodeNotExistException extends SystemDataNotExistException {

    public ProcessNodeNotExistException(String message) {
        super(message);
    }
}