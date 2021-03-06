package com.dagger4j.server.netty;

import com.dagger4j.server.common.BootStrap;
import com.dagger4j.server.netty.handler.HttpBaseHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author laotang
 * @date 2017/10/30
 */
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static Logger logger = LoggerFactory.getLogger(HttpChannelInitializer.class);

    private BootStrap bootStrap;
    private SslContext sslContext;

    public HttpChannelInitializer(BootStrap bootStrap) {
        this.bootStrap = bootStrap;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline channelPipeline = socketChannel.pipeline();
        // 开启https
        if(bootStrap.isSslEnabled()) {
            sslContext = bootStrap.getSslContext();
            if (sslContext != null) {
                channelPipeline.addLast(sslContext.newHandler(socketChannel.alloc()));
            }
        }
        // 为http响应内容添加gizp压缩器
        if (bootStrap.isEnableGzip()) {
            channelPipeline.addLast(new HttpContentCompressor());
        }
        // HttpServerCodec包含了默认的HttpRequestDecoder(请求消息解码器)和HttpResponseEncoder(响应解码器)
        channelPipeline.addLast(new HttpServerCodec());
        //目的是将多个消息转换为单一的request或者response对象
        channelPipeline.addLast(new HttpObjectAggregator(Integer.MAX_VALUE));
        //目的是支持异步大文件传输
        channelPipeline.addLast(new ChunkedWriteHandler());
        channelPipeline.addLast(new HttpServerExpectContinueHandler());
        if (bootStrap.isEnableCors()) {
            CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin().allowNullOrigin().allowCredentials().build();
            channelPipeline.addLast(new CorsHandler(corsConfig));
        }
//        channelPipeline.addLast(new HttpFilterRuleHandler());
        // 真正处理HTTP业务逻辑的地方,针对每个TCP连接创建一个新的ChannelHandler实例
        channelPipeline.addLast(new HttpBaseHandler(bootStrap));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.warn(cause.getMessage(), cause);
        ctx.fireExceptionCaught(cause);
    }
}
