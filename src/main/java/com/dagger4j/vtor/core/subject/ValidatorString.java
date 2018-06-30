package com.dagger4j.vtor.core.subject;


import com.dagger4j.exception.ValidatorException;
import com.dagger4j.kit.ObjectKit;
import com.dagger4j.vtor.annotation.Vtor;

import java.lang.reflect.Field;

public final class ValidatorString {

	public static void validator(Object obj, Field field, Vtor validator) throws Exception {

		String str = (String) ObjectKit.getFieldValue(obj, field);
		boolean paramNull = (null == str || "".equals(str) || str.length() == 0);
		boolean isEmpty = validator.isEmpty();		//默认为true
		String desc = "".equals(validator.message()) ? field.getName().toString() : validator.message();

		if (!isEmpty && paramNull) {
			throw new ValidatorException(desc + "不能为空!");
		}

		if (!"".equals(validator.value()) && paramNull) {
			ObjectKit.setField(obj, field, validator.value());
		}

		if (!paramNull && validator.length() > 0) {
			if (str.length() > validator.length()) {
				throw new ValidatorException(desc + "长度超出指定的范围");
			}
		}

		if (validator.oid() && !isEmpty) {
			if (paramNull) {
				throw new ValidatorException(desc + "不能为空!");
			}
			int len = str.length();
			if (len != 24) {
				throw new ValidatorException(desc + "长度不符合ObjectId规定!");
			}
			for (int i = 0; i < len; i++) {
				char c = str.charAt(i);
				if ((c < '0') || (c > '9')) {
					if ((c < 'a') || (c > 'f')) {
						if ((c < 'A') || (c > 'F')) {
							throw new ValidatorException(desc + " is not MongoDB ObjectId!");
						}
					}
				}
			}
		}
	}
}
