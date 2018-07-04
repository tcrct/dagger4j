package com.dagger4j.db.mongodb.utils;

import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.ContextValueFilter;
import com.dagger4j.db.IdEntity;
import com.dagger4j.utils.DataType;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

/**
 *
 * @author Created by laotang
 * @date createed in 2018/4/20.
 */
public class MongodbEncodeValueFilter implements ContextValueFilter {

    private Document document;

    public MongodbEncodeValueFilter() {

    }

    public MongodbEncodeValueFilter(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    @Override
    public Object process(BeanContext context, Object object, String name, Object value) {
        if(object != null && null != value) {
            System.out.println(context.getField().getName());
            System.out.println(context.getBeanClass().getName() +"               "+object + "           " + name + "           " + value + "      " + value.getClass()+"                  "+document.hashCode());
        }
        if(null == value) {
           return value;
        }
        Class type = value.getClass();
        if (DataType.isString(type) &&
                (IdEntity.ID_FIELD.equals(name) || IdEntity.ENTITY_ID_FIELD.equals(name))) {
            String idString = (String)value;
            if(ObjectId.isValid(idString)) {
                document.put(name, new ObjectId(idString));
                return new ObjectId(idString);
            }
        }
        else if(DataType.isShort(type) || DataType.isShortObject(type)) {
            document.put(name, Short.parseShort(value.toString()));
            return Short.parseShort(value.toString());
        }
        else if(DataType.isInteger(type) || DataType.isIntegerObject(type)) {
            document.put(name, Integer.parseInt(value.toString()));
            return Integer.parseInt(value.toString());
        }
        else if(DataType.isLong(type) || DataType.isLongObject(type)) {
            document.put(name, Long.parseLong(value.toString()));
            return Long.parseLong(value.toString());
        }
        else if(DataType.isDouble(type) || DataType.isDoubleObject(type)) {
            document.put(name, Double.valueOf(value.toString()));
            return Double.parseDouble(value.toString());
        }
        else if(DataType.isDate(value.getClass()) || DataType.isTimestamp(value.getClass())) {
            document.put(name, (Date)value);
            return (Date)value;
        }
        document.put(name, value);
        return value;
    }

}
