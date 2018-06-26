package com.dagger4j.db.mongodb.client;

import com.dagger4j.db.IClient;
import com.mongodb.MongoClient;

/**
 * @author Created by laotang
 * @date createed in 2018/6/26.
 */
public class MongoClientAdapter implements IClient<MongoClient> {


    @Override
    public String getId() {
        return null;
    }

    @Override
    public MongoClient getClient() {
        return null;
    }
}
