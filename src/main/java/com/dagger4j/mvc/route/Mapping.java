package com.dagger4j.mvc.route;

import java.util.List;

/**
 * @author Created by laotang
 * @date createed in 2018/6/12.
 */
public class Mapping {

    private String value;       // action映射的路径值
    private String desc;        // action的简要说明
    private int order;            // 排序
    private long timeout = 0L; //请求过期时间
    private List<ValidationParam> paramList;  // 验证的参数集合

    public Mapping(String value, String desc, int order, long timeout, List<ValidationParam> paramList) {
        this.value = value;
        this.desc = desc;
        this.order = order;
        this.timeout = timeout;
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
}
