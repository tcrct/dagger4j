package com.dagger4j.mvc.http.decoder;

import com.dagger4j.kit.ToolsKit;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author laotang
 * @date 2017/10/31
 */
public class GetDecoder extends AbstractDecoder<Map<String, List<String>>> {

    public GetDecoder(FullHttpRequest request) {
        super(request);
    }

    @Override
    public Map<String, List<String>> decoder() throws Exception {
        String url = request.uri();
        //先解码
        url = QueryStringDecoder.decodeComponent(url, HttpConstants.DEFAULT_CHARSET);
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
        Map<String,List<String>> map =  queryStringDecoder.parameters();
        if(ToolsKit.isNotEmpty(map)) {
            paramsMap.putAll(map);
        }
        return paramsMap;
    }

    private String sanitizeUri(String url) {
        try {
            url = URLDecoder.decode(url, CharsetUtil.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            try {
                url = URLDecoder.decode(url, CharsetUtil.ISO_8859_1.name());
            } catch (UnsupportedEncodingException e1) {
                throw new RuntimeException(e1);
            }
        }
        return url;
    }
}
