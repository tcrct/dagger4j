package com.dagger4j.mvc.core;

import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;

/**
 * Created by laotang on 2018/6/15.
 */
public abstract class Controller {
    private IRequest request;
    private IResponse response;

    public void init(IRequest request, IResponse response) {
        this.request = request;
        this.response = response;
    }
}
