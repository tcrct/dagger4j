package com.dagger4j.mvc.annotation;


import com.dagger4j.mvc.core.Interceptor;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Before {
	Class<? extends Interceptor>[] value();
}
