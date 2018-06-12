package com.dagger4j.mvc.http.handler;

import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 处理器链工厂
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public class HandlerChainFactory {

    /**
     * 前置处理器集合
     */
    private static List<IHandler> beforeHandlerList = new ArrayList<>();
    public static List<IHandler> getBeforeHandlerList() {
        return beforeHandlerList;
    }
    public static void setBeforeHandler(IHandler beforeHandler) {
        HandlerChainFactory.beforeHandlerList .add(beforeHandler);
    }

    /**
     * 后置处理器集合
     */
    private static List<IHandler> afterHandlerList = new ArrayList<>();
    public static List<IHandler> getAfterHandlerList() {
        return beforeHandlerList;
    }
    public static void setAfterHandler(IHandler afterHandler) {
        HandlerChainFactory.afterHandlerList .add(afterHandler);
    }


    /**
     * 执行前置(请求到达Controller前)的所有请求处理器
     * @param target    请求的URI地址
     * @param request   请求对象
     * @param response  响应对象
     * @throws MvcException
     */
    public static void doBeforeHandler(String target, IRequest request, IResponse response) throws MvcException {
        for (Iterator<IHandler> it = getBeforeHandlerList().iterator(); it.hasNext();) {
            it.next().doHandler(target, request, response);
        }
    }

    /**
     * 执行后置(请求到达Controller前)的所有响应处理器
     * @param target    请求的URI地址
     * @param request   请求对象
     * @param response  响应对象
     * @throws MvcException
     */
    public static void doAfterHandler(String target, IRequest request, IResponse response) throws MvcException {
        for (Iterator<IHandler> it = getAfterHandlerList().iterator(); it.hasNext();) {
            it.next().doHandler(target, request, response);
        }
    }
}
