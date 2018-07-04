package com.dagger4j.db.mongodb.convert.encode;


import com.dagger4j.db.annotation.Tran;
import com.dagger4j.kit.ObjectKit;

import java.lang.reflect.Field;

public abstract class Encoder {

    protected Field field;
    protected Object value;

    public Encoder(Object value, Field field){
        this.field = field;
        this.value = ObjectKit.getFieldValue(value, field);
    }

    public boolean isNull(){
        return null == value;
    }

    public boolean isTran(){
        return field.isAnnotationPresent(Tran.class);
    }

    public Field getField() {
        return field;
    }

    public abstract String getFieldName();

    public abstract Object getValue();
	
}
