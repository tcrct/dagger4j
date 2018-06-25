package com.dagger4j.db.mongodb.common;

import com.dagger4j.db.annotation.IdEntity;
import com.dagger4j.kit.ClassKit;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Created by laotang
 * @date createed in 2018/6/25.
 */
public class MongoBaseDao<T> extends MongoAdapter<T> {

    private final static Logger logger = LoggerFactory.getLogger(MongoBaseDao.class);

    protected Class<T> cls;
    protected DB mongoDB;
    protected DBCollection coll;
    protected MongoDatabase mongoDatabase;
    protected MongoCollection<Document> collection;
    protected DBObject keys;


    public MongoBaseDao(Class<T> cls){
        init(MongoClientKit.duang().getDefaultDB(),
                MongoClientKit.duang().getDefaultMongoDatabase(),
                cls);
    }

    public MongoBaseDao(DB db, MongoDatabase database, Class<T> cls) {
        init(db, database, cls);
    }

    /**
     * 初始化引用实例
     * @param db            数据库实例
     * @param database  数据库名称
     * @param cls             集合类对象
     */
    private void init(DB db, MongoDatabase database, Class<T> cls){
        boolean isExtends = ClassKit.isExtends(cls, IdEntity.class.getCanonicalName());
        if(!isExtends){
            throw new RuntimeException("the "+cls.getCanonicalName()+" is not extends "+ IdEntity.class.getCanonicalName() +", exit...");
        }
        this.cls = cls;
        try{
            mongoDB = db;
            mongoDatabase = database;
            // 根据类名或指定的name创建表名
            String entityName = ClassKit.getEntityName(cls);
            coll = mongoDB.getCollection(entityName);
            collection = mongoDatabase.getCollection(entityName);
            keys = MongoUtils.convert2DBFields(ClassKit.getFields(cls));
            MongoIndexUtils.createIndex(coll, cls);
        } catch(Exception e){
            e.printStackTrace();
            logger.error(coll.getFullName()+" Create Index Fail: " + e.getMessage());
        }
    }

    @Override
    public <T> T save(T entity) throws Exception {
        return null;
    }

    @Override
    public long update(MongoQuery query, MongoUpdate update) throws Exception {
        return 0;
    }

    @Override
    public <T> T findOne(MongoQuery query) throws Exception {
        return null;
    }
}
