package com.dagger4j.mvc.http.enums;


/**
 * Created by laotang on 2017/4/20.
 */
public enum ConstEnums {

    INPUTSTREAM_STR_NAME("dagger4j_inputstream_str", "以JSON或XML方式提交参数时，暂存在Request里的key"),
    REQUEST_TIMEOUT("3", "默认的请求过期时间"),
    CLASS_URL_PROTOCOL_FILE_FIELD("file", "calss类文件URL对象的类型"),
    CLASS_URL_PROTOCOL_JAR_FIELD("jar", "calss类文件URL对象的类型"),
    DEFAULT_ENCODING("utf-8", "默认的编码格式"),
    BASE_PACKAGE_PAGE("base.package.path", "要扫描的类路径"),
    PRODUCT_APPID("product.appid", "项目APPID"),
    PRODUCT_CODE("product.code", "项目简单称"),
    JAR_PREFIX("jar.prefix", "是扫描的jar包文件名前缀"),
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
