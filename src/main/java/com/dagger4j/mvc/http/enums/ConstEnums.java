package com.dagger4j.mvc.http.enums;


/**
 * Created by laotang on 2017/4/20.
 */
public enum ConstEnums {

    INPUTSTREAM_STR_NAME("dagger4j_inputstream_str", "以JSON或XML方式提交参数时，暂存在Request里的key"),
    REQUEST_TIMEOUT("3", "默认的请求过期时间"),
    ;


    private final String value;
    private final String desc;
    /**
     * Constructor.
     */
    private ConstEnums(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    /**
     * Get the value.
     * @return the value
     */
    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
