package com.dagger4j.db.mongodb.convert.encode;


import com.dagger4j.db.mongodb.convert.EncodeConvetor;
import com.dagger4j.kit.ToolsKit;

import java.lang.reflect.Field;

/**
 * Vo对象属性转换
 * @author laotang
 */
public class VoEncoder extends Encoder {

    public VoEncoder( Object value, Field field ) {
        super(value, field);
    }

    @Override
    public String getFieldName() {
        return ToolsKit.getFieldName(field);
    }

    @Override
    public Object getValue() {
        return EncodeConvetor.convetor(value);
    }
}
