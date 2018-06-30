package com.dagger4j.vtor.core.subject;

import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.vtor.annotation.Vtor;

import java.lang.reflect.Field;

public final class ValidatorInteger{
	
	public static void validator(Object obj, Field field, Vtor validator) throws Exception {

		int number = 0;		
		String desc = "".equals(validator.message()) ? field.getName().toString() : validator.message();
		double[] range = (null == validator.range() || validator.range()[0] == 0) ? null : validator.range();
		String tmpValue = (String) ObjectKit.getFieldValue(obj, field);
		if(null == tmpValue || "".equals(tmpValue) || tmpValue.length() == 0) return;
		try{
			number = Integer.parseInt(tmpValue);
		}catch(Exception ex){
			ex.printStackTrace();
			throw new ValidatorException(desc+"不是数字!");
		}
		
		if( null != range){
			if(number < range[0] || number > range[1]) {
				throw new ValidatorException(desc+"数值不在允许的["+ (int)range[0] +"]-["+  (int)range[1] +"]范围内!");	
			}
		}
		
		try {
			String valueStr = ("".equals(validator.value()) && null != validator.value()) ? "" : validator.value();
			if(valueStr.length() > 0 && number == 0) {
				number =  Integer.parseInt(valueStr);
			}
			ObjectKit.setField(obj, field, number);
		} catch (Exception e) {
			throw new ValidatorException("填充"+ desc+"时出错： "+ e.getMessage());
		}
	}
}
