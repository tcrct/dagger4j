package com.dagger4j.vtor.core;


import com.dagger4j.kit.ClassKit;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.mvc.scan.ScanClassFactory;
import com.dagger4j.vtor.annotation.Vtor;
import com.dagger4j.vtor.annotation.VtorVo;
import com.dagger4j.vtor.annotation.VtorVoColl;
import com.dagger4j.vtor.core.template.AbstractValidatorTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VtorFactory {

	private static final Map<String,Field[]> map = new HashMap<String, Field[]>();

	private static Map<Class<?>, AbstractValidatorTemplate> VALIDATOR_HANDLE_MAP = new HashMap<>();

	private static void init() {
	    String packagePath= AbstractValidatorTemplate.class.getPackage().getName();
        List<Class<?>> validatorHandleList = ScanClassFactory.getAllClass(packagePath, null);
		for(Class<?> clazz : validatorHandleList) {
            if(!ClassKit.supportInstance(clazz)) {
                continue;
            }
			AbstractValidatorTemplate validatorTemplate = ObjectKit.newInstance(clazz);
			VALIDATOR_HANDLE_MAP.put(validatorTemplate.annotationClass(), validatorTemplate);
		}
	}

	/**
	 * 按单个注解进行验证
	 * @param annotationType		注解类型
	 * @param parameterType			参数类型
	 * @param paramName				参数名称
	 * @param paramValue				参数值
	 * @throws Exception
	 */
	public static void validator(Annotation annotationType,  Class<?> parameterType,  String paramName, Object paramValue) throws Exception {
		if(ToolsKit.isEmpty(VALIDATOR_HANDLE_MAP)) {
			init();
		}
		Class<? extends Annotation> annotationClass = annotationType.annotationType();
		if(ToolsKit.isNotEmpty(VALIDATOR_HANDLE_MAP)) {
			VALIDATOR_HANDLE_MAP.get(annotationClass).vaildator(annotationType, parameterType, paramName, paramValue);
		}
	}

    /**
     * 验证bean
     * @param bean
     * @throws Exception
     */
    public static void validator(Object bean) throws Exception {
        Class<?> beanClass = bean.getClass();
        String beanName = bean.getClass().getName();
        Annotation[] annotations = beanClass.getAnnotations();
        // 没注解里直接退出
        if(ToolsKit.isEmpty(annotations)) {
            return;
        }

        Field[] fields = map.get(beanName);
        if( null == fields){
            fields = beanClass.getDeclaredFields();
        }

        boolean isValidatorBean = false;
        for(int i=0; i<fields.length; i++){
            Field field = fields[i];
            Annotation[] annotationArray = field.getAnnotations();
            for(Annotation annotation : annotationArray) {
                if(VALIDATOR_HANDLE_MAP.containsKey(annotation.getClass())) {
                    Object fieldValue = ObjectKit.getFieldValue(bean, field);
                    Class<?> fieldType = field.getType();
                    String fieldName = field.getName();
                    validator(annotation, fieldType, fieldName, fieldValue);
                    isValidatorBean = true;
                }
            }
        }
        if(isValidatorBean){
            map.put(beanName, fields);
        }
    }


	/**
	 *
	 * @param bean
	 * @throws Exception
	 */
	public static void validator2(Object bean) throws Exception {
		
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
