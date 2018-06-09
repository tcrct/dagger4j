package com.dagger4j.exception;

/**
 *  框架验证信息时异常
 * @author laotang
 * @date 2017/11/2
 */
public class VerificationException extends RuntimeException {

    public VerificationException() {
        super();
    }

    public VerificationException(String msg) {
        super(msg);
    }

    public VerificationException(String msg , Throwable cause) {
        super(msg, cause);
    }

}