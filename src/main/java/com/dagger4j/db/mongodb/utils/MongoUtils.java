package com.dagger4j.db.mongodb.utils;

import com.alibaba.fastjson.JSONObject;
import com.dagger4j.db.DbClientFactory;
import com.dagger4j.db.IdEntity;
import com.dagger4j.db.mongodb.client.MongoClientAdapter;
import com.dagger4j.db.mongodb.common.MongoDao;
import com.dagger4j.db.mongodb.convert.EncodeConvetor;
import com.dagger4j.db.mongodb.convert.decode.MongodbDecodeValueFilter;
import com.dagger4j.exception.MongodbException;
import com.dagger4j.kit.ClassKit;
import com.dagger4j.kit.ToolsKit;
import com.dagger4j.utils.DataType;
import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Created by laotang
 * @date on 2017/11/21.
 */
public class MongoUtils {

    private static Logger logger = LoggerFactory.getLogger(MongoUtils.class);

    private static ConcurrentMap<String, MongoDao<?>> MONGODAO_MAP = new ConcurrentHashMap<>();


    public static Object toObjectIds(Object values) {
        if(values instanceof Object[]){
            List<ObjectId> idList = new ArrayList<ObjectId>();
            Object[] tmp = (Object[]) values;
            for (Object value : tmp) {
                if (value != null) {
                    boolean isObjectId = ToolsKit.isValidDaggerId(value.toString());
                    if (isObjectId) {
                        ObjectId dbId = new ObjectId(value.toString());
                        idList.add(dbId);
                    }
                }
            }
            return idList;
        } else {
            boolean isObjectId = ToolsKit.isValidDaggerId(values.toString());
            if (isObjectId) {
                return new ObjectId(values.toString());
            } else {
                throw new MongodbException("toObjectId is Fail: ["+values+"] is not ObjectId or Empty");
            }
        }
    }

    /**
     *  将取出的类属性字段转换为Mongodb的DBObject
     * @param fields
     * @return
     */
    public static DBObject convert2DBFields(Field[] fields) {
        if (ToolsKit.isEmpty(fields)) {
            return null;
        }
        DBObject dbo = new BasicDBObject();
        for (int i = 0; i < fields.length; i++) {
            dbo.put(fields[i].getName(), true);
        }
        return dbo;
    }

    public static DBObject convert2DBFields(Collection<String> coll) {
        if (ToolsKit.isEmpty(coll)) {
            return null;
        }
        DBObject fieldsObj = new BasicDBObject();
        for (Iterator<String> it = coll.iterator(); it.hasNext();) {
            fieldsObj.put(it.next(), true);
        }
        return fieldsObj;
    }

    public static <T> T toBson(Object obj) {
        if(null == obj) {
            throw new MongodbException("toBson is fail:  obj is null");
        }
        try {
            return DataType.isBaseType(obj.getClass()) ? (T)obj : (T) EncodeConvetor.convetor(obj);
        } catch (Exception e) {
            throw new MongodbException("toBson is fail: " + e.getMessage(), e);
        }
    }

    public static <T> T toEntity(Document document, Class<?> clazz) {
        try {
            String json = JSONObject.toJSONString(document, new MongodbDecodeValueFilter());
            if(ToolsKit.isNotEmpty(json)) {
                return (T) ToolsKit.jsonParseObject(json, clazz);
            } return null;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将_id改为id字段
     * @param document
     * @return
     */
    private static Document convert2EntityId(Document document) {
        try {
            if (ToolsKit.isEmpty(document) || ToolsKit.isEmpty(document.get(IdEntity.ID_FIELD))) {
                return document;
            } else {
                document.put(IdEntity.ENTITY_ID_FIELD, document.get(IdEntity.ID_FIELD).toString());
            }
        } catch (ClassCastException e) {
                /*如果转换出错直接返回原本的值,不做任何处理*/
        }
        document.remove(IdEntity.ID_FIELD);
        return document;
    }

    /**
     * 将id字段更改为_id
     * @param document
     * @return
     */
    public static Document convert2ObjectId(Document document) {
        if(ToolsKit.isEmpty(document)) {
            throw  new MongodbException("convert2ObjectId is fail: document is null");
        }
        String id = document.getObjectId(IdEntity.ENTITY_ID_FIELD).toString();
        if (ToolsKit.isEmpty(id)) {
            id = document.getObjectId(IdEntity.ID_FIELD).toString();
        }
        if (ToolsKit.isNotEmpty(id)) {
            document.put(IdEntity.ID_FIELD, MongoUtils.toObjectIds(id));
        }
        return document;
    }

    /**
     * 根据Entity类取出MongoDao
     *@param dbClientId           多数据源时，指定的数据源客户端代号标识字符串
     * @param cls           继承了IdEntity的类
     * @param <T>
     * @return
     */
    public static <T> MongoDao<T> getMongoDao(String dbClientId, Class<T> cls){
        String key = ClassKit.getEntityName(cls);
        key = ToolsKit.isNotEmpty(dbClientId) ? dbClientId+"_" + key : key;
        MongoDao<?> dao = MONGODAO_MAP.get(key);
        if(null == dao){
            try {
                MongoClientAdapter clientAdapter = DbClientFactory.getMongoDbClient(dbClientId);
                MongoClient mongoClient = clientAdapter.getClient();
                String dbName = clientAdapter.getDbConnect().getDatabase();
                DB db = mongoClient.getDB(dbName);
                MongoDatabase database = mongoClient.getDatabase(dbName);
                dao = new MongoDao<T>(db, database, cls);
                MONGODAO_MAP.put(key, dao);
            } catch (Exception e) {
                logger.warn("getMongoDao is fail: " + e.getMessage(), e);
            }
        }
        return (MongoDao<T>)dao;
    }

    /**
     * 构建排序对象
     * @param fieldName 要排序的字段
     * @param orderBy	排序字符串，asc(1)或desc(-1)
     * @return
     */
    public static DBObject builderOrder(String fieldName, String orderBy){
        if(ToolsKit.isEmpty(fieldName) || ToolsKit.isEmpty(orderBy)) {
            return null;
        }
        if(ToolsKit.isEmpty(orderBy)){
            return BasicDBObjectBuilder.start(IdEntity.ID_FIELD, -1).get();		//默认用OID时间倒序
        }else{
            return BasicDBObjectBuilder.start(fieldName, "message".equalsIgnoreCase(orderBy) ? -1 : 1).get();
        }
    }

}
