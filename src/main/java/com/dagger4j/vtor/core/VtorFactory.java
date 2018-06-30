package com.dagger4j.vtor.core;


import com.dagger4j.vtor.annotation.Vtor;
import com.dagger4j.vtor.annotation.VtorVo;
import com.dagger4j.vtor.annotation.VtorVoColl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class VtorFactory {

	private static final Map<String,Field[]> map = new HashMap<String, Field[]>();


	/**
	 * 按单个注解进行验证
	 * @param annotationType		注解类型
	 * @param parameterType			参数类型
	 * @param paramName				参数名称
	 * @param paramValue				参数值
	 * @throws Exception
	 */
	public static void validator(Annotation annotationType,  Class<?> parameterType,  String paramName, Object paramValue) throws Exception {

	}


	/**
	 *
	 * @param bean
	 * @throws Exception
	 */
	public static void validator(Object bean) throws Exception {
		
		String beanName = bean.getClass().getName();
		
		Field[] fields = map.get(beanName);
		if( null == fields){
			fields = bean.getClass().getDeclaredFields();			
		}
		for(int i=0; i<fields.length; i++){
			Field field = fields[i];
			Validators valid = validatorValue(bean, field);
			if(null == valid) continue;
			valid.validator();
		}
		
		if(!map.containsKey(beanName) && null != fields){
			map.put(beanName, fields);
		}
		
//		for(Iterator<Entry<String, Field[]>> it = map.entrySet().iterator(); it.hasNext();){
//			Entry<String, Field[]> entry = it.next();
//			System.out.print(entry.getKey()+"                  ");
//			Field[] fs = entry.getValue();
//			for(Field f : fs){
//				System.out.print(f.getName());
//			}
//			System.out.println("");
//		}
			
	}
	
	private static Validators validatorValue(Object obj, Field field) throws Exception {
		Validators valid = null;
		if(null != field.getAnnotation(Vtor.class)){
			valid = new ValidatorProperty(field, obj);
		} else if ( null != field.getAnnotation(VtorVo.class)){
			valid = new ValidatorVo(field, obj);
		} else if(null != field.getAnnotation(VtorVoColl.class)){
			valid = new ValidatorVoColl(field, obj);
		}
		return valid;
	}
	
}
