package com.dagger4j.server.netty.handler;

import com.dagger4j.exception.VerificationException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.server.common.BootStrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author laotang
 * @date 2017/10/30
 */
public class HttpBaseHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static Logger logger = LoggerFactory.getLogger(HttpBaseHandler.class);
    private BootStrap bootStrap;

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest request) throws Exception {
        try {
            FullHttpRequest httpRequest = request.copy();
            verificationRequest(httpRequest);
            new RequestHandler(ctx, httpRequest).run();
        }catch (VerificationException ve) {
            logger.warn(ve.getMessage());
//            ReturnDto<String> returnDto = new ReturnDto<>();
//            HeadDto headDto = new HeadDto();
//            headDto.setUri(request.uri());
//            headDto.setTimestamp(System.currentTimeMillis());
//            headDto.setRet(500);
//            headDto.setMsg(ve.getMessage());
//            returnDto.setData(ve.getMessage());
//            returnDto.setHead(headDto);
//            ResponseUtils.buildFullHttpResponse(ctx, request, ToolsKit.toJsonString(returnDto));
        }
    }

    /**
     * 验证请求是否正确
     * @return
     */
    private void verificationRequest(FullHttpRequest request) {

        // 保证解析结果正确,否则直接退出
        if (!request.decoderResult().isSuccess()) {
            throw new VerificationException("request decoderParams is not success, so exit...");
        }

        // 支持的的请求方式
        String method = request.method().toString();
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        if(ToolsKit.isEmpty(httpMethod)) {
            throw new VerificationException("request method["+ httpMethod.toString() +"] is not support, so exit...");
        }

        // uri是有长度的
        String uri = request.uri();
        if (uri == null || uri.trim().length() == 0) {
            throw new VerificationException("request uri length is 0 , so exit...");
        } else {
            // 判断是否有参数，有参数则先截掉参数
            if(uri.contains("?")) {
                uri = uri.substring(0, uri.indexOf("?"));
            }
            // 如果包含有.则视为静态文件访问
            if(uri.contains(".")) {
                throw new VerificationException("not support static file access, so exit...");
            }
        }
    }
}
