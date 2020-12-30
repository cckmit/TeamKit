package org.team4u.workflow.domain.instance.exception;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 流程动作节点不存在异常
 *
 * @author jay.wu
 */
public class ProcessActionNodeNotExistException extends SystemDataNotExistException {

    public ProcessActionNodeNotExistException(String message) {
        super(message);
    }
}