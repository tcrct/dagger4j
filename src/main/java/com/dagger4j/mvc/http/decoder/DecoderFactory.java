package com.dagger4j.mvc.http.decoder;

import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.enums.ContentTypeEnums;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.http.*;

/**
 * Created by laotang on 2017/10/31.
 */
public class DecoderFactory {


    public static AbstractDecoder create(String method, String contentType, FullHttpRequest request)  throws Exception{
        if(ToolsKit.isEmpty(request)) {
            throw new DecoderException("request is null");
        }
        if(ToolsKit.isEmpty(method)) {
            throw new DecoderException("request method is null");
        }

        AbstractDecoder decoder = null;
        if(HttpMethod.GET.name().equalsIgnoreCase(method)) {
            decoder = new GetDecoder(request);
        }
        else if(HttpMethod.POST.name().equalsIgnoreCase(method)) {
            if(contentType.contains(ContentTypeEnums.JSON.getValue())) {
                decoder = new JsonDecoder(request);
            } else if(contentType.contains(ContentTypeEnums.XML.getValue())) {
                decoder = new XmlDecoder(request);
            } else if (contentType.contains(ContentTypeEnums.MULTIPART.getValue())) {
                decoder = new MultiPartPostDecoder(request.copy());
            } else {
                // 都不符合以上的默认为post form表单提交
                decoder = new PostDecoder(request.copy());
            }
        } else if (HttpMethod.OPTIONS.name().equalsIgnoreCase(method)) {
            decoder = new OptionsDecoder(request);
        }

        return decoder;

    }

}
