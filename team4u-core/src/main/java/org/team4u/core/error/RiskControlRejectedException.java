package org.team4u.core.error;

public class RiskControlRejectedException extends BusinessException {

    public RiskControlRejectedException() {
    }

    public RiskControlRejectedException(String message) {
        super(message);
    }

    public RiskControlRejectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RiskControlRejectedException(Throwable cause) {
        super(cause);
    }
}