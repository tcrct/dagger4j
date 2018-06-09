package com.dagger4j.server.netty.handler;

import com.dagger4j.mvc.http.HttpRequest;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;
import com.dagger4j.utils.DaggerId;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AsciiString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 请求处理器，一线程一处理
 * Created by laotang on 2018/6/7.
 */
public class RequestHandler implements Runnable {

    private final ChannelHandlerContext ctx;
    private final FullHttpRequest fullHttpRequest;
    private IRequest iRequest;
    private IResponse iResponse;
    public RequestHandler(ChannelHandlerContext ctx, FullHttpRequest request) {
        this.ctx = ctx;
        this.fullHttpRequest = request;
        init();
    }

    private void init() {
        IRequest  iRequest = new HttpRequest.Builder().context(ctx).request(fullHttpRequest).requestId(new DaggerId().toString()).build();
        IResponse iResponse = new HttpResponse.Builder().context(ctx).headerDate(new Ai).build();
    }

    @Override
    public void run() {

    }
}
