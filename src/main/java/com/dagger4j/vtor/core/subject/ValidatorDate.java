package com.dagger4j.vtor.core.subject;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.vtor.annotation.Vtor;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ValidatorDate{
	
	
	public static void validator(Object obj, Field field, Vtor validator) throws Exception {

		String desc = "".equals(validator.message()) ? field.getName().toString() : validator.message();
		boolean isEmpty = validator.isEmpty();
		long dateLong = 0l;
		boolean paramNull = false;
		String formatStr =  validator.formatDate();
		
		try {
			Date date = (Date) ObjectKit.getFieldValue(obj, field);
			paramNull =( null == date) ? true : false;
			if(null != date){
				dateLong =  date.getTime();
			}
		} catch (Exception e) {
			throw new ValidatorException(desc+"不是java.util.Date!");
		}
		
		
		if(!isEmpty){
			throw new ValidatorException(desc+"不能为空!");
		}
		
		try {			
			if(!paramNull && dateLong > 0l){
				SimpleDateFormat df = new SimpleDateFormat(formatStr);
				String value = df.format(new Date(dateLong));
				ObjectKit.setField(obj, field, df.parse(value));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ValidatorException(desc+"不能正确转换为["+formatStr+"]日期格式!");
		}
		
		
	}

}
