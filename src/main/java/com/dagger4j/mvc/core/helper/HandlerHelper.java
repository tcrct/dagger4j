package com.dagger4j.mvc.core.helper;

import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;
import com.dagger4j.mvc.http.handler.IHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 处理器链工厂
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public class HandlerHelper {

    /**
     * 前置处理器集合
     */
    public static List<IHandler> beforeHandlerList = new ArrayList<>();
    public static void setBeforeHandlerList(List<IHandler> beforeHandlerList) {
        HandlerHelper.beforeHandlerList .addAll(beforeHandlerList);
    }

    /**
     * 后置处理器集合
     */
    public static List<IHandler> afterHandlerList = new ArrayList<>();
    public static void setAfterHandlerList(List<IHandler> afterHandlerList) {
        HandlerHelper.afterHandlerList .addAll(afterHandlerList);
    }


    /**
     * 执行前置(请求到达Controller前)的所有请求处理器
     * @param target    请求的URI地址
     * @param request   请求对象
     * @param response  响应对象
     * @throws MvcException
     */
    public static void doBeforeChain(String target, IRequest request, IResponse response) throws MvcException {
        for (Iterator<IHandler> it = beforeHandlerList.iterator(); it.hasNext();) {
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
    public static void doAfterChain(String target, IRequest request, IResponse response) throws MvcException {
        for (Iterator<IHandler> it = afterHandlerList.iterator(); it.hasNext();) {
            it.next().doHandler(target, request, response);
        }
    }
}
