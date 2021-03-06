package com.dagger4j.exception;

import com.dagger4j.kit.ToolsKit;

/**
 *  请求超时异常
 * @author laotang
 * @date 2017/11/2
 */
public class TimeoutException extends AbstractDaggerException implements IException {

    public TimeoutException() {
        super();
    }

    public TimeoutException(String msg) {
        super(msg);
    }

    public TimeoutException(String msg , Throwable cause) {
        super(msg, cause);
    }

    @Override
    public int getCode() {
        return ExceptionEnums.REQUEST_TIMEOUT.getCode();
    }

    @Override
    public String getMessage() {
        if(ToolsKit.isEmpty(super.getMessage())) {
            return ExceptionEnums.REQUEST_TIMEOUT.getMessage();
        } else {
            return ExceptionEnums.REQUEST_TIMEOUT.getMessage() + ": " + super.getMessage();
        }
    }
}