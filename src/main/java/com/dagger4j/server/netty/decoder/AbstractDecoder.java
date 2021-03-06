package com.dagger4j.server.netty.decoder;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.netty.handler.codec.http.multipart.DefaultHttpDataFactory.MINSIZE;

/**
 *
 * @author laotang
 * @date 2017/10/31
 */
public abstract class AbstractDecoder<T> {

    protected static HttpDataFactory HTTP_DATA_FACTORY = new DefaultHttpDataFactory(MINSIZE, HttpConstants.DEFAULT_CHARSET);

    protected static String[] EMPTY_ARRAYS = {};

    protected FullHttpRequest request;
    protected  Map<String,Object> requestParamsMap;

    public AbstractDecoder(FullHttpRequest request) {
        this.request = request;
        requestParamsMap = new ConcurrentHashMap<>();
    }

    protected void parseValue2List(Map<String,List<String>> params, String key, String value) {
        if( params.containsKey(key) ) {
            params.get(key).add(value);
        } else {
            List<String> valueList = new ArrayList<>();
            valueList.add(value);
            params.put(key, valueList);
        }
    }

    public abstract  T decoder() throws Exception;

}
