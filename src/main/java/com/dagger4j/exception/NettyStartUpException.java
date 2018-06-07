package com.dagger4j.exception;

/**
 *  Dagger4j框架启动时异常
 * @author laotang
 * @date 2017/11/2
 */
public class NettyStartUpException extends RuntimeException {

    public NettyStartUpException() {
        super();
    }

    public NettyStartUpException(String msg) {
        super(msg);
    }

    public NettyStartUpException(String msg , Throwable cause) {
        super(msg, cause);
    }

}