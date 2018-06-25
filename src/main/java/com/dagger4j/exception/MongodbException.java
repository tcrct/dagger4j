package com.dagger4j.exception;

/**
 *  Dagger4j框架启动时异常
 * @author laotang
 * @date 2017/11/2
 */
public class MongodbException extends RuntimeException {

    public MongodbException() {
        super();
    }

    public MongodbException(String msg) {
        super(msg);
    }

    public MongodbException(String msg , Throwable cause) {
        super(msg, cause);
    }

}