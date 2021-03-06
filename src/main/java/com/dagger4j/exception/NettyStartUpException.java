package com.dagger4j.exception;

import com.dagger4j.kit.ToolsKit;

/**
 *  Dagger4j框架启动时异常
 * @author laotang
 * @date 2017/11/2
 */
public class NettyStartUpException extends AbstractDaggerException implements IException {

    public NettyStartUpException() {
        super();
    }

    public NettyStartUpException(String msg) {
        super(msg);
    }

    public NettyStartUpException(String msg , Throwable cause) {
        super(msg, cause);
    }

    @Override
    public int getCode() {
        return ExceptionEnums.NETTY_STARTUP_ERROR.getCode();
    }

    @Override
    public String getMessage() {
        if(ToolsKit.isEmpty(super.getMessage())) {
            return ExceptionEnums.NETTY_STARTUP_ERROR.getMessage();
        } else {
            return ExceptionEnums.NETTY_STARTUP_ERROR.getMessage() + ": " + super.getMessage();
        }
    }
}