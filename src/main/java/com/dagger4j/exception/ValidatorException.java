package com.dagger4j.exception;

import com.dagger4j.kit.ToolsKit;

/**
 *  框架验证信息时异常
 * @author laotang
 * @date 2017/11/2
 */
public class ValidatorException extends AbstractDaggerException implements IException {

    public ValidatorException() {
        super();
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getCode() {
        return ExceptionEnums.PARAM_ERROR.getCode();
    }

    @Override
    public String getMessage() {
        if(ToolsKit.isEmpty(super.getMessage())) {
            return ExceptionEnums.PARAM_ERROR.getMessage();
        } else {
            return ExceptionEnums.PARAM_ERROR.getMessage() + ": " + super.getMessage();
        }
    }

}