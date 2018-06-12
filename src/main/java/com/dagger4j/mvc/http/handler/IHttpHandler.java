package com.dagger4j.mvc.http.handler;

import java.util.List;

/**
 * 处理器接口
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public interface IHttpHandler {

    List<IHandler> getBeforeHandlerList();

    List<IHandler> getAfterHandlerList();

}
