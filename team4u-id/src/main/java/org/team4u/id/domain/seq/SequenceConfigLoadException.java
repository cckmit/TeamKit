package org.team4u.id.domain.seq;

import org.team4u.base.error.DataNotExistException;

/**
 * 序号配置加载异常
 *
 * @author jay.wu
 */
public class SequenceConfigLoadException extends DataNotExistException {

    public SequenceConfigLoadException() {
    }

    public SequenceConfigLoadException(String message) {
        super(message);
    }

    public SequenceConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public SequenceConfigLoadException(Throwable cause) {
        super(cause);
    }
}