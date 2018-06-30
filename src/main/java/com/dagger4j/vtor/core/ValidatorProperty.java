package com.dagger4j.vtor.core;

import com.dagger4j.utils.DataType;
import com.dagger4j.vtor.annotation.Vtor;
import com.dagger4j.vtor.core.subject.*;

import java.lang.reflect.Field;


public final class ValidatorProperty extends Validators{

	protected Vtor validator;
	
	public ValidatorProperty(Field field, Object obj) {
		super(field, obj);
		this.validator = field.getAnnotation(Vtor.class);
	}

	public Vtor getValidator() {
		return validator;
	}
	
	@Override
	public void validator() throws Exception {
		if( DataType.isString(getFieldType())){
			ValidatorString.validator(obj, field, getValidator());
		}
		
		if( DataType.isInteger(getFieldType()) || DataType.isIntegerObject(getFieldType()) ){
			ValidatorInteger.validator(obj, field, getValidator());
		}
		
		if( DataType.isDouble(getFieldType()) || DataType.isDoubleObject(getFieldType())){
			ValidatorDouble.validator(obj, field, getValidator());
		}
		
		if( DataType.isFloat(getFieldType()) || DataType.isFloatObject(getFieldType())){
			ValidatorFloat.validator(obj, field, getValidator());
		}
		
		if( DataType.isDate(getFieldType())){
			ValidatorDate.validator(obj, field, getValidator());
		}
	}
}
