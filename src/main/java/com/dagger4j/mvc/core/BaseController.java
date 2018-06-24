package com.dagger4j.mvc.core;

import com.dagger4j.kit.Prop;
import com.dagger4j.kit.PropKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.http.IRequest;
import com.dagger4j.mvc.http.IResponse;
import com.dagger4j.mvc.http.enums.ConstEnums;
import com.dagger4j.mvc.render.Render;
import com.dagger4j.mvc.render.TextRender;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by laotang on 2018/6/15.
 */
public abstract class BaseController {

    private static Logger logger = LoggerFactory.getLogger(BaseController.class);

    private IRequest request;
    private IResponse response;
    private Render render;

    public void init(IRequest request, IResponse response) {
        this.request = request;
        this.response = response;
        if(PropKit.getBoolean("debug")) {
            printRequestInfo();
        }
    }

    private void printRequestInfo() {
        logger.info("******************************************************************************");
        logger.info("###########RequestDate:   " + ToolsKit.formatDate(getRequestDate(), PropKit.get(ConstEnums.DEFAULT_DATE_FORMAT.getValue(), "yyyy-MM-dd HH:mm:ss")));
        logger.info("###########RequestHeader: " + request.getHeader(HttpHeaderNames.USER_AGENT.toString()));
        logger.info("###########RequestURL:    " + request.getRequestURL());
        logger.info("###########RemoteMethod:  " + request.getMethod());
        logger.info("###########getContentType:  " + request.getContentType());
        logger.info("###########RequestValues: " + ToolsKit.toJsonString(getAllParams()));
        logger.info("******************************************************************************");
    }

    /**
     * 取出请求日期时间
     * @return
     */
    private Date getRequestDate() {
        String d = request.getHeader(HttpHeaderNames.DATE.toString());
        if (ToolsKit.isEmpty(d)) {
            return new Date();
        }
        try {
            return new Date(Long.parseLong(d));
        } catch (Exception e) {
            return ToolsKit.parseDate(d, PropKit.get(ConstEnums.DEFAULT_DATE_FORMAT.getValue(), "yyyy-MM-dd HH:mm:ss"));
        }
    }

    /**
     * 取出所有请求的参数
     * @return
     */
    private Map<String, Object> getAllParams() {
        Map<String, Object> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        if (ToolsKit.isNotEmpty(requestParams)) {
            for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = iter.next();
                if(ConstEnums.INPUTSTREAM_STR_NAME.toString().equalsIgnoreCase(name)) {
                    continue;
                }
                String[] values = requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                params.put(name, valueStr);
            }
        }

        Enumeration<String> attributeNames = request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = attributeNames.nextElement();
            if(ConstEnums.INPUTSTREAM_STR_NAME.toString().equalsIgnoreCase(name) ||
                    name.contains(".")) {
                continue;
            }
            params.put(name, request.getAttribute(name));
        }
        return params;
    }


    public Render getRender() {
        if(null == render) {
            render = new TextRender("request is not set render");
        }
        return render;
    }


    /**
     * 返回文本格式
     * @param text
     */
    public void returnText(String text) {
        render = new TextRender(text);
    }
}
