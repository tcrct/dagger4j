package com.dagger4j.vtor.core;

import java.lang.reflect.Field;


public final class ValidatorVo extends Validators{

	public ValidatorVo(Field field, Object obj) {
		super(field, obj);
	}

	@Override
	public void validator() throws Exception {
		VtorFactory.validator2(getValue());
	}
}
