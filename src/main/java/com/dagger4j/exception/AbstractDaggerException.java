package com.dagger4j.exception;

/**
 * @author Created by laotang
 * @date createed in 2018/7/5.
 */
public abstract class AbstractDaggerException extends RuntimeException implements IException {

    protected int code = IException.FAIL_CODE;
    protected String message = IException.FAIL_MESSAGE;

    AbstractDaggerException() {
        super();
    }

    AbstractDaggerException(String msg) {
        super(msg);
        setMessage(msg);
    }

    public AbstractDaggerException(String msg , Throwable cause) {
        super(msg, cause);
        setMessage(msg);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
