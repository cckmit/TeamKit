package org.team4u.workflow.domain.definition.exception;

import org.team4u.workflow.domain.ProcessSystemException;

/**
 * 流程定义不存在异常
 *
 * @author jay.wu
 */
public class ProcessDefinitionNotExistException extends ProcessSystemException {

    public ProcessDefinitionNotExistException(String message) {
        super(message);
    }
}
