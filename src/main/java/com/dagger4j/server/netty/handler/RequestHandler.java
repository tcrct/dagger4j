package com.dagger4j.server.netty.handler;

import com.dagger4j.exception.NettyStartUpException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.MvcMain;
import com.dagger4j.mvc.http.HttpRequest;
import com.dagger4j.mvc.http.HttpResponse;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 请求处理器，一线程一处理
 * Created by laotang on 2018/6/7.
 */
public class RequestHandler implements Runnable {

    private final ChannelHandlerContext ctx;
    private final FullHttpRequest fullHttpRequest;
    public RequestHandler(ChannelHandlerContext ctx, FullHttpRequest request) {
        this.ctx = ctx;
        this.fullHttpRequest = request;
        init();
    }

    private void init() {

    }

    @Override
    public void run() {
        IRequest  iRequest = HttpRequest.build(ctx, fullHttpRequest);
        IResponse iResponse = HttpResponse.build(iRequest);
        if(ToolsKit.isEmpty(iRequest) || ToolsKit.isEmpty(iResponse)) {
            throw new NettyStartUpException("build dagger4j request or response fail");
        }
        // 执行请求任务
        MvcMain.doTask(iRequest, iResponse);
    }
}
