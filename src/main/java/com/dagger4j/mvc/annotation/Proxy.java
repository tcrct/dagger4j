package com.dagger4j.mvc.annotation;

import java.lang.annotation.*;

/**
 *  代理注解
 * @author Created by laotang
 * @date on 2017/11/16.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Proxy {
    Class<?> value() default Object.class;
}