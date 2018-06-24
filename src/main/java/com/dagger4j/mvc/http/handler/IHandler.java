package com.dagger4j.mvc.http.handler;

import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;

/**
 * 处理器接口，抛出异常时中止流程
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public interface IHandler {

    void doHandler(String target, IRequest request, IResponse response) throws MvcException;
}
