package com.dagger4j.exception;

import java.util.logging.Logger;

/**
 *  框架验证信息时异常
 * @author laotang
 * @date 2017/11/2
 */
public class ValidatorException extends RuntimeException {

    public ValidatorException() {
        super();
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Logger logger) {
        super(message);
    }

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

}