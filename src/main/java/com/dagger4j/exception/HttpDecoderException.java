package com.dagger4j.exception;

/**
 * Created by laotang on 2018/6/9.
 */
public class HttpDecoderException extends RuntimeException {

    public HttpDecoderException() {
        super();
    }

    public HttpDecoderException(String msg) {
        super(msg);
    }

    public HttpDecoderException(String msg , Throwable cause) {
        super(msg, cause);
    }
}


