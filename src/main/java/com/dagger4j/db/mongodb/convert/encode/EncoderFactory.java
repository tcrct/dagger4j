package com.dagger4j.db.mongodb.convert.encode;


import com.dagger4j.db.annotation.Id;
import com.dagger4j.db.annotation.Vo;
import com.dagger4j.db.annotation.VoColl;

import java.lang.reflect.Field;

public final class EncoderFactory {
    
    public static Encoder create(Object obj, Field field){
		Encoder encoder = null;
		if (null != field.getAnnotation(Id.class)) {
//			encoder = new IdEncoder(obj, field);
		} else if ((null != field.getAnnotation(Vo.class))) {
//			encoder = new VoEncoder(obj, field);
		} else if ((null != field.getAnnotation(VoColl.class)) ) {
//			encoder = new VoCollEncoder(obj, field);
		} else {
//			encoder = new PropertyEncoder(obj, field);
		}
		return encoder;
	}
}
