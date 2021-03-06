package com.dagger4j.exception;

import com.dagger4j.kit.ToolsKit;

/**
 *  mvc组件运行异常
 * @author laotang
 * @date 2017/11/2
 */
public class MvcException extends AbstractDaggerException implements IException {

    public MvcException() {
        super();
    }

    public MvcException(String msg) {
        super(msg);
    }

    public MvcException(String msg , Throwable cause) {
        super(msg, cause);
    }

    @Override
    public int getCode() {
        return ExceptionEnums.MVC_ERROR.getCode();
    }

    @Override
    public String getMessage() {
        if(ToolsKit.isEmpty(super.getMessage())) {
            return ExceptionEnums.MVC_ERROR.getMessage();
        } else {
            return ExceptionEnums.MVC_ERROR.getMessage() + ": " + super.getMessage();
        }
    }
}