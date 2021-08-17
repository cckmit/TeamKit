package org.team4u.base.bean.provider;

import org.team4u.base.error.SystemDataNotExistException;

/**
 * 无法找到bean异常
 *
 * @author jay.wu
 */
public class NoSuchBeanDefinitionException extends SystemDataNotExistException {

    public NoSuchBeanDefinitionException() {
    }

    public NoSuchBeanDefinitionException(String message) {
        super(message);
    }

    public NoSuchBeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchBeanDefinitionException(Throwable cause) {
        super(cause);
    }
}