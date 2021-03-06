package com.dagger4j.mvc.route;

import java.util.List;

/**
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public class RequestMapping {

    public static final String VALUE_FIELD = "value";
    public static final String DESC_FIELD = "desc";
    public static final String ORDER_FIELD = "order";
    public static final String TIMEOUT_FIELD = "timeout";
    public static final String METHOD_FIELD = "method";

    private String value;       // action映射的路径值
    private String desc;        // action的简要说明
    private int order;            // 排序
    private long timeout = 0L; //请求过期时间
    private List<ValidationParam> paramList;  // 验证的参数集合
    private String method;  // 请求方式

    public RequestMapping() {

    }

    public RequestMapping(String value, String desc, int order, long timeout, String method, List<ValidationParam> paramList) {
        this.value = value;
        this.desc = desc;
        this.order = order;
        this.timeout = timeout;
        this.method = method;
        this.paramList = paramList;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public int getOrder() {
        return order;
    }

    public long getTimeout() {
        return timeout;
    }

    public List<ValidationParam> getParamList() {
        return paramList;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setParamList(List<ValidationParam> paramList) {
        this.paramList = paramList;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
