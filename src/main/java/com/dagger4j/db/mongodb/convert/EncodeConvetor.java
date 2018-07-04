package com.dagger4j.db.mongodb.convert;

import com.dagger4j.db.annotation.Id;
import com.dagger4j.db.annotation.Vo;
import com.dagger4j.db.annotation.VoColl;
import com.dagger4j.db.mongodb.convert.encode.*;
import com.dagger4j.kit.ClassKit;
import com.dagger4j.kit.ToolsKit;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author Created by laotang
 * @date createed in 2018/3/26.
 */
public class EncodeConvetor {

    private final static Logger logger = LoggerFactory.getLogger(EncodeConvetor.class);

    public static Document convetor(Object object) {
        if(ToolsKit.isEmpty(object)) {
            throw new NullPointerException("Entity Convetor Document Fail: " + object.getClass().getCanonicalName() + " is null!");
        }
        Document document = new Document();
        Field[] fields = ClassKit.getFields(object.getClass());
        if(ToolsKit.isNotEmpty(fields)) {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                Encoder encoder = parser(object, field);
                if(!encoder.isNull() && !encoder.isTran()) {        //不为null且没有Tran注解
                    document.put(encoder.getFieldName(), encoder.getValue());
                }
            }
        }
        return document;
    }


    private static Encoder parser(Object obj, Field field) {
        Encoder encoder = null;
        if( null != field.getAnnotation(Id.class)){
            encoder = new IdEncoder(obj, field);
        } else if( null != field.getAnnotation(Vo.class)) {
            encoder = new VoEncoder(obj, field);
        } else if (null != field.getAnnotation(VoColl.class)) {
            encoder = new VoCollEncoder(obj, field);
        } else {
            encoder = new PropertyEncoder(obj, field);
        }
        return encoder;
    }
}
