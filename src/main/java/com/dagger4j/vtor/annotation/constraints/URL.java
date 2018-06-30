package com.dagger4j.vtor.annotation.constraints;

import java.lang.annotation.*;

/**
 * 邮箱地址验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface URL {

    String message() default "url地址不正确";

    String protocol() default "";

    String host() default "";

    int port() default -1;
}
