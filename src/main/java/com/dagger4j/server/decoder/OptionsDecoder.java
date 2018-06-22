package com.dagger4j.server.decoder;

import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;
import java.util.Map;

/**
 *
 * @author laotang
 * @date 2017/10/31
 */
public class OptionsDecoder extends AbstractDecoder<Map<String, List<String>>> {

    public OptionsDecoder(FullHttpRequest request) {
        super(request);
    }

    @Override
    public Map<String, List<String>> decoder() throws Exception {
        return paramsMap;
    }
}
