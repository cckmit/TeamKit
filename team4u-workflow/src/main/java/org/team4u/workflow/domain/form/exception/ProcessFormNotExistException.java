package org.team4u.workflow.domain.form.exception;

import org.team4u.base.error.DataNotExistException;

/**
 * 表单索引不存在异常
 *
 * @author jay.wu
 */
public class ProcessFormNotExistException extends DataNotExistException {

    public ProcessFormNotExistException(String message) {
        super(message);
    }
}