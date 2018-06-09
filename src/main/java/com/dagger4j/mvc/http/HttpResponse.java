package com.dagger4j.mvc.http;

import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Created by laotang on 2018/6/9.
 */
public class HttpResponse implements IResponse {

    private IRequest request;

    private HttpResponse(IRequest iRequest){
        request = iRequest;
    }

    @Override
    public void addHeader(String key, String value) {

    }

    @Override
    public void setHeader(String key, String value) {

    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public Map<String, String> getHeaders() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setContentType(String contentType) {

    }

    @Override
    public void write(Object returnObj) {

    }

    @Override
    public void setContentLength(int contentLength) {

    }

    @Override
    public File getDownloadFile() {
        return null;
    }

    public static class Builder {
        private IRequest request;

        public Builder() {
        }

        public Builder request(IRequest iRequest) {
            request = iRequest;
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(request);
        }

    }
}
