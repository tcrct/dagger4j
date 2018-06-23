package com.dagger4j.mvc.http.enums;


import com.dagger4j.mvc.annotation.*;

import java.lang.annotation.Annotation;

/**
 * Created by laotang on 2017/4/20.
 */
public enum ConstEnums {

    INPUTSTREAM_STR_NAME("dagger4j_inputstream_str", "以JSON或XML方式提交参数时，暂存在Request里的key"),
    REQUEST_TIMEOUT("3000", "默认的请求过期时间毫秒"),
    CLASS_URL_PROTOCOL_FILE_FIELD("file", "calss类文件URL对象的类型"),
    CLASS_URL_PROTOCOL_JAR_FIELD("jar", "calss类文件URL对象的类型"),
    DEFAULT_ENCODING("utf-8", "默认的编码格式"),
    BASE_PACKAGE_PATH("base.package.path", "要扫描的类路径"),
    PRODUCT_APPID("product.appid", "项目APPID"),
    PRODUCT_CODE("product.code", "项目简单称"),
    JAR_PREFIX("jar.prefix", "是扫描的jar包文件名前缀"),
    REPORT_MAPPING_KEY("/dagger/report", "框架报告映射路径前缀"),
    DAGGER_INPUTSTREAM_STR_FIELD("dagger_inputstream_str", "框架报告映射路径前缀"),
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

    /**
     * 注解类枚举
     * 注解类，是否需要实例化(BeanHelper时使用)，说明
     */
    public enum ANNOTATION_CLASS {
        CONTROLLER_ANNOTATION(Controller.class, true,"所有Controller类的注解，必须在类添加该注解否则框架忽略扫描"),
        SERVICE_ANNOTATION(Service.class, true,"所有Service类的注解，必须在类添加该注解否则框架忽略扫描"),
        ENTITY_ANNOTATION(Entity.class, true,"所有Entity类的注解，必须在类添加该注解否则框架忽略扫描"),
        PLUGIN_ANNOTATION(Plugin.class, false, "所有Plugin类的注解，必须在类添加该注解否则框架忽略扫描, 但不需要在BeanHelper里进行实例化"),
        HANDLER_ANNOTATION(Handler.class, false, "所有Handler类的注解，必须在类添加该注解否则框架忽略扫描, 但不需要在BeanHelper里进行实例化"),
        ;

        Class<? extends Annotation> clazz;
        String name;
        // 是否需要实例化， true为需要
        boolean instance;
        String desc;

        private ANNOTATION_CLASS(Class<? extends Annotation> clazz, boolean instance, String desc) {
            this.clazz = clazz;
            this.instance = instance;
            this.desc = desc;
        }

        public Class<? extends Annotation> getClazz() {
            return clazz;
        }

        public String getName() {
            return clazz.getName();
        }

        /**
         * 是否需要实例化， true为需要
         * @return
         */
        public boolean getInstance() {
            return instance;
        }

        public String desc() {
            return desc;
        }
    }


    public enum HANDLE {
        BEFORE, AFTER;
    }



}
