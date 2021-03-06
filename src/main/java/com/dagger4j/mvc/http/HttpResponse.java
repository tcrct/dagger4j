package com.dagger4j.mvc.http;

import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ConstEnums;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by laotang on 2018/6/9.
 */
public class HttpResponse implements IResponse {

    private IRequest request;
    private Map<String,String> headers;
    private int status;
    private String contentType;
    private String charset;
    private Object returnObj = null;

    private HttpResponse(IRequest iRequest){
        request = iRequest;
        headers = new HashMap<>();
        charset = HttpConstants.DEFAULT_CHARSET.toString();
        status = HttpResponseStatus.OK.code();
        returnObj = null;
    }

    public static HttpResponse build(IRequest request) {
        return new HttpResponse(request);
    }


    @Override
    public String getRequestId() {
        return request.getRequestId();
    }

    @Override
    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }

    @Override
    public Map<String, String> getHeaders() {
        return headers;
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
        this.charset = charset;
    }

    @Override
    public String getCharacterEncoding() {
        return charset;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void write(Object returnObj) {
        this.returnObj = returnObj;
    }

    @Override
    public Object getBody() {
        return returnObj;
    }

    @Override
    public void setContentLength(int contentLength) {

    }

    @Override
    public String toString() {
        if(null != returnObj) {
            return ToolsKit.toJsonString(returnObj);
        } else{
            Map<String, String> map = new HashMap<>();
            map.put("hello", ConstEnums.FRAMEWORK_OWNER.getValue());
            return ToolsKit.toJsonString(ToolsKit.buildReturnDto(null, map));
        }
    }

    @Override
    public File getFile() {
        return  isFile() ? (File)returnObj : null;
    }

    @Override
    public boolean isFile() {
        return returnObj instanceof File;
    }

}
