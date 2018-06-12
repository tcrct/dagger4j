package com.dagger4j.mvc.http;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * Created by laotang on 2018/6/9.
 */
public class HttpResponse implements IResponse {

    private IRequest request;
    private int status;

    private HttpResponse(IRequest iRequest){
        request = iRequest;
    }

    public static HttpResponse build(IRequest request) {
        return new HttpResponse(request);
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
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getStatus() {
        return status;
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

}
