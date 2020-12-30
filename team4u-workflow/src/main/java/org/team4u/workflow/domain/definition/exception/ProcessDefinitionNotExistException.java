package org.team4u.workflow.domain.definition.exception;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 流程定义不存在异常
 *
 * @author jay.wu
 */
public class ProcessDefinitionNotExistException extends SystemDataNotExistException {

    public ProcessDefinitionNotExistException(String message) {
        super(message);
    }
}
