package com.dagger4j.vtor.core;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public final class ValidatorVoColl extends Validators{

	public ValidatorVoColl(Field field, Object obj) {
		super(field, obj);
	}

	@Override
	public void validator() throws Exception {
		Class<?> fieldType = field.getType();
		if(fieldType.isArray()){
			
		} else {
			ParameterizedType paramType = (ParameterizedType)field.getGenericType();
			Type[] type = paramType.getActualTypeArguments();
			if(type.length == 1){
				validatorCollection();
			}else if (type.length == 2){
				validatorMap();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	private void validatorCollection() throws Exception {
		Collection coll = (Collection)getValue();
		for(Iterator it = coll.iterator(); it.hasNext();){
			Object obj = it.next();
			if(null != obj){
				VtorFactory.validator2(obj);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void validatorMap() throws Exception {
		Map map = (Map)getValue();
		for(Iterator<Entry<String, Object>> it = map.entrySet().iterator(); it.hasNext();){
			Entry<String, Object> entry = it.next();
			Object obj = entry.getValue();
			if(null != obj){
				VtorFactory.validator2(obj);
			}
		}
	}
}
