package com.dagger4j.server.netty.handler;

import com.dagger4j.exception.AbstractDaggerException;
import com.dagger4j.exception.MvcException;
import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ThreadPoolKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.core.helper.RouteHelper;
import com.dagger4j.mvc.dto.HeadDto;
import com.dagger4j.mvc.dto.ReturnDto;
import com.dagger4j.mvc.http.IResponse;
import com.dagger4j.mvc.route.Route;
import com.dagger4j.server.common.BootStrap;
import com.dagger4j.utils.IpUtils;
import com.dagger4j.utils.WebKit;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author laotang
 * @date 2017/10/30
 */
public class HttpBaseHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static Logger logger = LoggerFactory.getLogger(HttpBaseHandler.class);
    private BootStrap bootStrap;

    public HttpBaseHandler(BootStrap bootStrap) {
        this.bootStrap = bootStrap;
    }

    @Override
    public void channelRead0(final ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        IResponse response = null;
        FutureTask<IResponse> futureTask = null;
        RequestTask requestTask = null;
        try {
            FullHttpRequest httpRequest = request.copy();
            verificationRequest(httpRequest);
            requestTask = new RequestTask(ctx, httpRequest);
            futureTask = ThreadPoolKit.execute(requestTask);
            // 是否开发模式，如果是则不指定超时
            if(bootStrap.isDevModel()) {
                response = futureTask.get();
            } else {
                // 等待结果返回，如果超出指定时间，则抛出TimeoutException, 默认时间为3秒
                response = futureTask.get(getTimeout(httpRequest.uri()), TimeUnit.MILLISECONDS);
            }
        } catch (TimeoutException e) {
            // 超时时，会执行该异常
            response = buildExceptionResponse(requestTask.getResponse(), new com.dagger4j.exception.TimeoutException(e.getMessage()));
            // 中止线程，参数为true时，会中止正在运行的线程，为false时，如果线程未开始，则停止运行
            futureTask.cancel(true);
        } catch (ValidatorException ve) {
            logger.warn(ve.getMessage());
            response = buildExceptionResponse(requestTask.getResponse(), ve);
        } catch (Exception e) {
            response = buildExceptionResponse(requestTask.getResponse(), new MvcException(e.getMessage(), e));
        } finally {
            if(null != request && null != response) {
                WebKit.recoverClient(ctx, request, response);
            }
        }
    }

    /**
     * 验证请求是否正确
     * @return
     */
    private void verificationRequest(FullHttpRequest request) {

        // 保证解析结果正确,否则直接退出
        if (!request.decoderResult().isSuccess()) {
            throw new ValidatorException("request decoderParams is not success, so exit...");
        }

        // 支持的的请求方式
        String method = request.method().toString();
        HttpMethod httpMethod = HttpMethod.valueOf(method);
        if(ToolsKit.isEmpty(httpMethod)) {
            throw new ValidatorException("request method["+ httpMethod.toString() +"] is not support, so exit...");
        }

        // uri是有长度的
        String uri = request.uri();
        if (uri == null || uri.trim().length() == 0) {
            throw new ValidatorException("request uri length is 0 , so exit...");
        } else {
            // 判断是否有参数，有参数则先截掉参数
            if(uri.contains("?")) {
                uri = uri.substring(0, uri.indexOf("?"));
            }
            // 如果包含有.则视为静态文件访问
            if(uri.contains(".")) {
                throw new ValidatorException("not support static file access, so exit...");
            }
        }
    }

    private long getTimeout(String target) {
        Route route = RouteHelper.getRouteMap().get(target);
        if(ToolsKit.isEmpty(route)) {
            // TODO... restful风格的URI确定不了，暂不能支持timeout设置，按默认值3秒
            route = null; //RouteHelper.getRestfulRouteMap().get(target);
        }
        return null != route ? route.getRequestMapping().getTimeout() : 3000L;
    }


    private IResponse buildExceptionResponse(IResponse httpResponse, AbstractDaggerException daggerException) {
        int code = daggerException.getCode();
        String message = daggerException.getMessage();
        ReturnDto<String> returnDto = new ReturnDto<>();
        returnDto.setData(null);
        HeadDto headDto = new HeadDto();
        headDto.setMsg(message);
        headDto.setRet(code);
        headDto.setTimestamp(System.currentTimeMillis());
        headDto.setRequestId(httpResponse.getRequestId());
        headDto.setClientIp(IpUtils.getLocalHostIP());
        returnDto.setHead(headDto);
        httpResponse.write(returnDto);
        httpResponse.setHeader("status", "200");
        return httpResponse;
    }
}
