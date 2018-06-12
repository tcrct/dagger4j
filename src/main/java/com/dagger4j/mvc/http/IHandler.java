package com.dagger4j.mvc.http;

import com.dagger4j.exception.MvcException;

/**
 * 处理器接口
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public interface IHandler {

    void doHandler(String target, IRequest request, IResponse response) throws MvcException;
}
