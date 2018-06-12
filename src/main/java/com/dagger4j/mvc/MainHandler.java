package com.dagger4j.mvc;

import com.dagger4j.exception.MvcException;
import com.dagger4j.mvc.http.handler.HandlerChainFactory;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;

import java.io.IOException;

/**
 * Created by laotang on 2018/6/10.
 * @author laotang
 */
public class MainHandler {

    //前置
    public static void startContextListener() {
        // 扫描所有的类
    }

    /**
     * 取得request所请求的资源路径。
     * <p>
     * 资源路径为<code>getRequestURI()</code>。
     * </p>
     * <p>
     * 注意，<code>URI</code>以<code>"/"</code>开始，如果无内容，则返回空字符串
     *             <code>URI</code>不能以<code>"/"</code>结束，如果存在， 则强制移除
     * </p>
     */
    private static String getResourcePath(IRequest request) {
        String target = request.getRequestURI();
        if(target.startsWith("/")) {
            return "";
        }
        if(target.endsWith("/")) {
            target = target.substring(0, target.length()-1);
        }
        // 验证该请求URI是否存在
//        if(!RouteFactory.verificationTarget(target)) {
//            return "";
//        }
        return target;
    }


    /**
     * 执行请求任务
     * @param request   请求对象
     * @param response 响应对象
     * @throws IOException
     * @throws MvcException
     */
    public static void doTask(IRequest request, IResponse response) throws MvcException {
        String target = getResourcePath(request);
        try {
            HandlerChainFactory.doBeforeHandler(target, request, response);
            HandlerChainFactory.doAfterHandler(target, request, response);
        } catch (Exception e) {
            response.setStatus(500);
        }
    }


}
