package com.dagger4j.mvc.http;

import com.dagger4j.exception.HttpDecoderException;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.decoder.AbstractDecoder;
import com.dagger4j.mvc.http.decoder.DecoderFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.DiskFileUpload;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by laotang on 2018/6/9.
 */
public class HttpRequest implements IRequest{

    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;
        DiskFileUpload.baseDirectory = null;
        DiskAttribute.deleteOnExitTemporaryFile = true;
        DiskAttribute.baseDirectory = null;
    }

    private ChannelHandlerContext ctx;
    private FullHttpRequest request;
    private String requestId;
    private Map<String, String> headers;
    private Map<String, Object> params;

    private HttpRequest(String id, ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        requestId = id;
        ctx = channelHandlerContext;
        request = fullHttpRequest;
        init();
    }

    private void init() {
        try {
            AbstractDecoder<Map<String, Object>> decoder = DecoderFactory.create(getMethod(), getContentType(), request);
            params = decoder.decoder();
        } catch (Exception e) {
            throw new HttpDecoderException(e.getMessage(), e);
        }
    }

    @Override
    public Object getAttribute(String name) {

        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public String getParameter(String name) {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    @Override
    public String getProtocol() {
        return request.protocolVersion().text();
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String getServerName() {
        return null;
    }

    @Override
    public int getServerPort() {
        return 0;
    }

    @Override
    public String getRemoteAddr() {
        return ctx.channel().remoteAddress().toString();
    }

    @Override
    public String getRemoteHost() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {

    }

    @Override
    public void removeAttribute(String name) {

    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getHeader(String name) {
        return getHeaderMap().get(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return new Vector(getHeaderMap().keySet()).elements();
    }

    @Override
    public String getMethod() {
        return request.method().name();
    }

    @Override
    public String getQueryString() {
        String url = getRequestURL();
        if (null != url && url.contains("?")) {
            return url.substring(url.indexOf("?") + 1);
        }
        return "";
    }

    @Override
    public String getRequestURI() {
        String url = getRequestURL();
        int pathEndPos = url.indexOf('?');
        return (pathEndPos < 0) ? url : url.substring(0, pathEndPos);
    }

    @Override
    public String getRequestURL() {
        return request.uri();
    }

    @Override
    public Map<String, String> getHeaderMap() {
        if(ToolsKit.isEmpty(headers)) {
            HttpHeaders httpHeaders = request.headers();
            if (!httpHeaders.isEmpty() && httpHeaders.size() > 0) {
                headers = new HashMap<>(httpHeaders.size());
                for(Iterator<Map.Entry<String,String>> it = httpHeaders.iteratorAsString(); it.hasNext();){
                    Map.Entry<String, String> entry = it.next();
                    headers.put(entry.getKey(), entry.getValue());
                }
            } else {
                this.headers = new HashMap<>();
            }
        }
        return headers;
    }


    public static class Builder {
        private ChannelHandlerContext channelHandlerContext;
        private FullHttpRequest fullHttpRequest;
        private String requestId;

        public Builder() {}

        public Builder context(ChannelHandlerContext ctx) {
            channelHandlerContext = ctx;
            return this;
        }

        public Builder request(FullHttpRequest request) {
            fullHttpRequest = request;
            return this;
        }

        public Builder id(String id) {
            requestId = id;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(requestId, channelHandlerContext, fullHttpRequest);
        }
    }
}
