package com.dagger4j.exception;

/**
 *  业务逻辑处理异常
 * @author laotang
 * @date 2017/11/2
 */
public class ServiceException extends AbstractDaggerException implements IException {

    private IException iException;

    public ServiceException() {
        this(ExceptionEnums.ERROR.getCode(), ExceptionEnums.ERROR.getMessage());
    }

    public ServiceException(String msg) {
        this(ExceptionEnums.ERROR.getCode(), msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(String msg , Throwable cause) {
        super(msg, cause);
        this.code = ExceptionEnums.ERROR.getCode();
    }

    public ServiceException(int code, String msg , Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public ServiceException(IException exception) {
        this(exception.getCode(), exception.getMessage());
        this.iException = exception;
    }

    public ServiceException(IException exception , Throwable cause) {
        this(exception.getCode(), exception.getMessage(), cause);
        this.iException = exception;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}