package com.dagger4j.mvc.http.handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laotang on 2018/6/12.
 */
public abstract class HandlerAdapter implements IHttpHandler {

    List<IHandler> beforeHandlerList = new ArrayList();
    List<IHandler> afterHandlerList = new ArrayList();


    public abstract void before(List<IHandler> beforeHandlerList);
    public abstract void after(List<IHandler> afterHandlerList);


    @Override
    public List<IHandler> getBeforeHandlerList() {
        return beforeHandlerList;
    }

    @Override
    public List<IHandler> getAfterHandlerList() {
        return afterHandlerList;
    }
}
