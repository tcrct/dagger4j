package com.dagger4j.db;

import com.dagger4j.db.mongodb.client.MongoClientAdapter;
import com.dagger4j.kit.ToolsKit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Created by laotang
 * @date createed in 2018/6/26.
 */
public class DbClientFactory {

    /**
     * 所有实例客户端的Pool，注意KEY的命令，要确保唯一性
     */
    private static ConcurrentMap<String, IClient> MYSQL_CLIENT_MAP1 = new ConcurrentHashMap<>();
    private static ConcurrentMap<String, MongoClientAdapter> MONGODB_CLIENT_MAP = new ConcurrentHashMap<>();
    private static String DEFAULT_MONGODB_ID;

    /**
     * 添加Mongodb数据库客户端到缓存池
     * @param dbClient
     */
    public static void setMongoClient(MongoClientAdapter clientAdapter) {
        if(ToolsKit.isEmpty(clientAdapter)) {
            throw new NullPointerException("db client adapter  is null");
        }
        String dbClientId = clientAdapter.getId();
        if(!MONGODB_CLIENT_MAP.containsKey(dbClientId)) {
            MONGODB_CLIENT_MAP.put(dbClientId, clientAdapter);
        }
    }

    /**
     * 取MongoDB默认的ClientId<br/>
     * 如果没有设置默认值，则以第一个实例作默认客户端
     * @return
     */
    public static String getMongoDefaultClientId() {
        return DEFAULT_MONGODB_ID;
    }
    public static void setMongoDefaultClientId(String defaultClientId) {
        DEFAULT_MONGODB_ID = defaultClientId;
    }

    /**
     * 根据名称返回客户端实例
     * @param dbClientId      保存在缓存池里的客户端的id
     * @return
     */
    public static MongoClientAdapter getMongoDbClient(String dbClientId) throws Exception {
        return MONGODB_CLIENT_MAP.get(dbClientId);
    }
    public static Map<String, MongoClientAdapter> getMongoDbClients() throws Exception {
        return MONGODB_CLIENT_MAP;
    }


}
