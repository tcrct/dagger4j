package com.dagger4j.vtor.annotation.constraints;

import java.lang.annotation.*;

/**
 * 表达式验证注解
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pattern {
    String regexp();
    String message() default "该Email地址不正确！";
}
