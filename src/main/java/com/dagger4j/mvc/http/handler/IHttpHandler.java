package com.dagger4j.mvc.http.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理器接口
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public interface IHttpHandler {

    void before(List<IHandler> beforeHandlerList);

    void after(List<IHandler> afterHandlerList);

}
