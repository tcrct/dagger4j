package com.dagger4j.vtor.annotation.constraints;

import java.lang.annotation.*;

/**
 * 是否DaggerId验证
 * @author Created by laotang
 * @date createed in 2018/6/30.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DaggerId {
    String message() default "id格式不正确！";
}
