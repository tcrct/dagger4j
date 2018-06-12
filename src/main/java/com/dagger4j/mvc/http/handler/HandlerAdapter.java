package com.dagger4j.mvc.http.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laotang on 2018/6/12.
 */
public abstract class HandlerAdapter implements IHttpHandler {

    List<IHandler> beforeHandlerList = new ArrayList();
    List<IHandler> afterHandlerList = new ArrayList();


    @Override
    public abstract void before(List<IHandler> beforeHandlerList);
    @Override
    public abstract void after(List<IHandler> afterHandlerList);

}
