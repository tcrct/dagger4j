package com.dagger4j.utils;

import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.IResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by laotang on 2018/6/23.
 */
public class WebKit {

    /**
     * 将请求结果返回到客户端
     * @param ctx                               context上下文
     * @param fullHttpRequest         netty请求对象
     * @param response                      自定义返回对象
     */
    public static void recoverClient(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, IResponse response) {
        if(response.isFile()) {
          builderResponseStream();
        } else {
            // 构建请求返回对象，并设置返回主体内容结果
            HttpResponseStatus status = response.getStatus() == HttpResponseStatus.OK.code() ? HttpResponseStatus.OK : HttpResponseStatus.INTERNAL_SERVER_ERROR;
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HTTP_1_1, status, Unpooled.copiedBuffer(response.toString(), HttpConstants.DEFAULT_CHARSET));
            builderResponseHeader(fullHttpResponse, response);
            ChannelFuture channelFutureListener = ctx.channel().writeAndFlush(fullHttpResponse);
            //如果不支持keep-Alive，服务器端主动关闭请求
            if (!HttpHeaders.isKeepAlive(fullHttpRequest)) {
                channelFutureListener.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private static void builderResponseHeader(FullHttpResponse fullHttpResponse, IResponse response) {
        HttpHeaders responseHeaders = fullHttpResponse.headers();
        responseHeaders.set(HttpHeaderNames.DATE.toString(), ToolsKit.getCurrentDateString());
        int readableBytesLength = 0;
        try {
            readableBytesLength = fullHttpResponse.content().readableBytes();
        } catch (Exception e) {}
        responseHeaders.set(HttpHeaderNames.CONTENT_LENGTH.toString(), readableBytesLength);
    }

    private static void builderResponseStream() {

    }

}
