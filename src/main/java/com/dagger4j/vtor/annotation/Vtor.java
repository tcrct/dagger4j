package com.dagger4j.vtor.annotation;

import com.dagger4j.vtor.annotation.constraints.*;

import java.lang.annotation.*;

//@Target({ElementType.FIELD,ElementType.METHOD})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Vtor {
	
	NotEmpty notEmpty();		// 是否允许值为null或空字串符 默认为允许
	 
	Length length();	 			// 长度，限制字符串长度
	 
	Range range();			// 取值范围，如[0,100] 则限制该值在0-100之间

	Max max();				// 最大值

	Min min();				// 最小值
	 
	Ymd date();		// 格式化日期(24小时制)
	 
	DaggerId daggerId();					// 是否是mongodb objectId，主要用于验证id

}



/**
ValidatorHandler.assertFalse=assertion failed

ValidatorHandler.assertTrue=assertion failed

ValidatorHandler.future=must be a future date

ValidatorHandler.length=length must be between {min} and {max}

ValidatorHandler.max=must be less than or equal to {value}

ValidatorHandler.min=must be greater than or equal to {value}

ValidatorHandler.notNull=may not be null

ValidatorHandler.past=must be a past date

ValidatorHandler.pattern=must match "{regex}"

ValidatorHandler.range=must be between {min} and {max}

ValidatorHandler.size=size must be between {min} and {max}
*/
