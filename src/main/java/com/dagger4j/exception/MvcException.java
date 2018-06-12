package com.dagger4j.exception;

/**
 *  Dagger4j框架启动时异常
 * @author laotang
 * @date 2017/11/2
 */
public class MvcException extends RuntimeException {

    public MvcException() {
        super();
    }

    public MvcException(String msg) {
        super(msg);
    }

    public MvcException(String msg , Throwable cause) {
        super(msg, cause);
    }

}