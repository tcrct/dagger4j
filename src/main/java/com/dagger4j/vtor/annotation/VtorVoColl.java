package com.dagger4j.vtor.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VtorVoColl {
	
	 String name() default "";
	 
}
